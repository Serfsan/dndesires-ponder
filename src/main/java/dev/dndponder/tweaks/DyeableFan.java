package dev.dndponder.tweaks;

import com.mojang.serialization.Codec;
import dev.dndponder.DnDPonder;
import dev.dndponder.DnDTarget;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.Optional;
import java.util.function.Supplier;

/**
* Dyeable Industrial Fan blades. D&D 2.3a-BETA registers 16 colored propeller models that just sit there and generated some warnings in the console.
* Personally, I don't think I will EVER dye fan blades. However....removing content wasn't my main goal so f it. This is mandatory for the server, but client-side you can join without this .java file. It's optional and you'll just see regular fans instead of rainbows.
*/
public final class DyeableFan {
    public static final ResourceLocation FAN_ID = ResourceLocation.fromNamespaceAndPath(DnDTarget.DND_MOD_ID, "industrial_fan");
    // -1 undyed btdubs
    public static final int NONE = -1;

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENTS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, DnDPonder.MOD_ID);

    public static final Supplier<AttachmentType<Integer>> COLOR = ATTACHMENTS.register("fan_color",
            () -> AttachmentType.builder(() -> NONE).serialize(Codec.INT).build());

    private static boolean active;

    private DyeableFan() {}

    public static boolean isActive() {
        return active;
    }

    public static void register(IEventBus modBus) {
        // The attachment type is always registered. However, if you dye your fan and uninstall this addon.
        ATTACHMENTS.register(modBus);
        modBus.addListener(DyeableFan::onRegisterPayloads);

        active = Tweaks.dyeableIndustrialFan && (Tweaks.dyeableFanOnAnyDnDVersion || DnDTarget.isTestedVersion());
        if (Tweaks.dyeableIndustrialFan && !active) {
            DnDPonder.LOGGER.warn("Dyeable Industrial Fan is off: it was built for Dreams n' Desires {} and {} is installed. "
                            + "Set dyeableFanOnAnyDnDVersion=true in dndesires_ponder-startup.toml to force it.",
                    DnDTarget.testedVersion(), DnDTarget.installedVersion().orElse("?"));
        }
        if (!active) return;

        NeoForge.EVENT_BUS.addListener(DyeableFan::onRightClickBlock);
        NeoForge.EVENT_BUS.addListener(DyeableFan::onChunkSent);
    }

    public static int colorOf(BlockEntity be) {
        return be.hasData(COLOR) ? be.getData(COLOR) : NONE;
    }

    private static boolean isFan(Level level, BlockPos pos) {
        Optional<Block> fan = BuiltInRegistries.BLOCK.getOptional(FAN_ID);
        return fan.isPresent() && level.getBlockState(pos).is(fan.get());
    }

    // interaction

    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        DyeColor dye = DyeColor.getColor(stack);
        if (dye == null) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        if (!isFan(level, pos)) return;
        BlockEntity be = level.getBlockEntity(pos);
        if (be == null || colorOf(be) == dye.getId()) return;

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.sidedSuccess(level.isClientSide));
        if (level.isClientSide) {
            // Send color to client and return sync
            be.setData(COLOR, dye.getId());
            return;
        }
        if (!(level instanceof ServerLevel serverLevel)) return;

        be.setData(COLOR, dye.getId());
        be.setChanged();
        if (!event.getEntity().getAbilities().instabuild) stack.shrink(1);
        level.playSound(null, pos, SoundEvents.DYE_USE, SoundSource.BLOCKS, 1f, 1f);
        ColorPayload payload = new ColorPayload(pos, dye.getId());
        for (ServerPlayer watcher : serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)) {
            sendIfSupported(watcher, payload);
        }
    }

    // I'm worried this will cause some latency if you make an RF Tools dimension of only furnaces or something.
    private static void onChunkSent(ChunkWatchEvent.Sent event) {
        ServerPlayer player = event.getPlayer();
        if (!player.connection.hasChannel(ColorPayload.TYPE)) return;    // player cant get  
        BlockEntityType<?> fanType = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(FAN_ID);
        if (fanType == null) return;
        for (BlockEntity be : event.getChunk().getBlockEntities().values()) {
            if (be.getType() != fanType) continue;                       // skip because not a fan
            int color = colorOf(be);
            if (color != NONE) {
                PacketDistributor.sendToPlayer(player, new ColorPayload(be.getBlockPos(), color));
            }
        }
    }

    //sync payload

    private static void sendIfSupported(ServerPlayer player, ColorPayload payload) {
        if (player.connection.hasChannel(ColorPayload.TYPE)) {
            PacketDistributor.sendToPlayer(player, payload);
        }
    }

    public record ColorPayload(BlockPos pos, int color) implements CustomPacketPayload {
        public static final Type<ColorPayload> TYPE =
                new Type<>(ResourceLocation.fromNamespaceAndPath(DnDPonder.MOD_ID, "fan_color"));
        public static final StreamCodec<ByteBuf, ColorPayload> STREAM_CODEC = StreamCodec.composite(
                BlockPos.STREAM_CODEC, ColorPayload::pos,
                ByteBufCodecs.VAR_INT, ColorPayload::color,
                ColorPayload::new);

        @Override
        public Type<? extends CustomPacketPayload> type() {
            return TYPE;
        }
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        // optional(): may skip this addon
        event.registrar("1").optional().playToClient(ColorPayload.TYPE, ColorPayload.STREAM_CODEC, DyeableFan::handleOnClient);
    }

    private static void handleOnClient(ColorPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            if (!level.isLoaded(payload.pos())) return;
            BlockEntity be = level.getBlockEntity(payload.pos());
            if (be != null) {
                be.setData(COLOR, payload.color());
                DnDPonder.LOGGER.debug("Fan at {} colored {} (sync)", payload.pos(), payload.color());
            }
        });
    }
}