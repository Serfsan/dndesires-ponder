package dev.dndponder.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.KineticBlockEntityRenderer;
import dev.dndponder.DnDPonder;
import dev.dndponder.tweaks.DyeableFan;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.CachedBuffers;
import net.createmod.catnip.render.SuperByteBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;


// The cog and propeller drawing is a copy of D&D's renderer, using D&D's own model files by ID; the only change is that a dyed fan draws its blades from this addon's split models with the dye color applied.
public final class DyedFanRenderer extends KineticBlockEntityRenderer<KineticBlockEntity> {    
    // PartialModel.of must run for model baking hence the static nature of this
    private static final PartialModel COG = partial("dndesires", "block/industrial_fan/cog");
    private static final PartialModel PROPELLER = partial("dndesires", "block/industrial_fan/propeller");
    private static final PartialModel SHAFT = partial(DnDPonder.MOD_ID, "block/industrial_fan/propeller_shaft");
    private static final PartialModel BLADES = partial(DnDPonder.MOD_ID, "block/industrial_fan/propeller_blades");

    public DyedFanRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    private static PartialModel partial(String namespace, String path) {
        return PartialModel.of(ResourceLocation.fromNamespaceAndPath(namespace, path));
    }

    static void init() {}

    @SuppressWarnings("unchecked")
    static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        if (!DyeableFan.isActive()) return;
        BlockEntityType<?> type = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(DyeableFan.FAN_ID);
        if (type == null) {
            DnDPonder.LOGGER.warn("{} block entity not found, dyeable fan blades disabled", DyeableFan.FAN_ID);
            return;
        }
        event.registerBlockEntityRenderer((BlockEntityType<KineticBlockEntity>) type, DyedFanRenderer::new);
    }


    //MASTER RENDERER

    // This check should only run at startup and by every resource reload. Honestly not as graceful as I would like. Sue me \o/

    private static final int CHECK_WINDOW_TICKS = 100;
    private static volatile int checksRemaining = CHECK_WINDOW_TICKS;

    static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener((ResourceManagerReloadListener) manager -> checksRemaining = CHECK_WINDOW_TICKS);
    }

    private static Field renderersField;
    private static boolean installFailed;
    private static boolean loggedInstall;

    static void onClientTick(ClientTickEvent.Post event) {
        if (checksRemaining <= 0 || installFailed || !DyeableFan.isActive()) return;
        Minecraft mc = Minecraft.getInstance();
        BlockEntityRenderDispatcher dispatcher = mc.getBlockEntityRenderDispatcher();
        if (dispatcher == null || mc.level == null) return;
        checksRemaining--;
        BlockEntityType<?> type = BuiltInRegistries.BLOCK_ENTITY_TYPE.get(DyeableFan.FAN_ID);
        if (type == null) return;

        try {
            if (renderersField == null) {
                renderersField = BlockEntityRenderDispatcher.class.getDeclaredField("renderers");
                renderersField.setAccessible(true);
            }
            @SuppressWarnings("unchecked")
            Map<BlockEntityType<?>, BlockEntityRenderer<?>> current =
                    (Map<BlockEntityType<?>, BlockEntityRenderer<?>>) renderersField.get(dispatcher);
            if (current.get(type) instanceof DyedFanRenderer) return;

            Map<BlockEntityType<?>, BlockEntityRenderer<?>> replaced = new HashMap<>(current);
            replaced.put(type, new DyedFanRenderer(new BlockEntityRendererProvider.Context(dispatcher,
                    mc.getBlockRenderer(), mc.getItemRenderer(), mc.getEntityRenderDispatcher(),
                    mc.getEntityModels(), mc.font)));
            renderersField.set(dispatcher, replaced);
            if (!loggedInstall) {
                loggedInstall = true;
                DnDPonder.LOGGER.info("Dyeable Industrial Fan renderer installed (replaced {})",
                        current.get(type) == null ? "nothing" : current.get(type).getClass().getName());
            }
        } catch (ReflectiveOperationException | RuntimeException e) {
            installFailed = true;
            DnDPonder.LOGGER.warn("Could not install the Dyeable Industrial Fan renderer; fans will render undyed", e);
        }
    }

    @Override
    protected void renderSafe(KineticBlockEntity be, float partialTicks, PoseStack ms, MultiBufferSource buffer,
                              int light, int overlay) {
        Level level = be.getLevel();
        if (level == null) return;
        BlockState state = be.getBlockState();
        if (!state.hasProperty(BlockStateProperties.FACING)) return;

        Direction direction = state.getValue(BlockStateProperties.FACING);
        VertexConsumer vb = buffer.getBuffer(RenderType.cutoutMipped());
        int lightOverall = LevelRenderer.getLightColor(level, be.getBlockPos());
        int lightInFront = LevelRenderer.getLightColor(level, be.getBlockPos().relative(direction));

        float time = AnimationTickHolder.getRenderTime(level);
        float speed = be.getSpeed() * 5;
        if (speed > 0) speed = Mth.clamp(speed, 80, 64 * 20);
        if (speed < 0) speed = Mth.clamp(speed, -64 * 20, -80);
        float angle = (time * speed * 3 / 10f) % 360;
        angle = angle / 180f * (float) Math.PI;

        SuperByteBuffer cog = CachedBuffers.partialFacing(COG, state, direction.getOpposite());
        standardKineticRotationTransform(cog, be, lightOverall).renderInto(ms, vb);

        int colorId = DyeableFan.colorOf(be);
        if (colorId == DyeableFan.NONE) {
            SuperByteBuffer propeller = CachedBuffers.partialFacing(PROPELLER, state, direction.getOpposite());
            kineticRotationTransform(propeller, be, direction.getAxis(), angle, lightInFront).renderInto(ms, vb);
            return;
        }

        SuperByteBuffer shaft = CachedBuffers.partialFacing(SHAFT, state, direction.getOpposite());
        kineticRotationTransform(shaft, be, direction.getAxis(), angle, lightInFront).renderInto(ms, vb);

        int rgb = DyeColor.byId(colorId).getTextureDiffuseColor();
        SuperByteBuffer blades = CachedBuffers.partialFacing(BLADES, state, direction.getOpposite());
        kineticRotationTransform(blades, be, direction.getAxis(), angle, lightInFront)
                .color((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF, 255)
                .renderInto(ms, vb);
    }
}