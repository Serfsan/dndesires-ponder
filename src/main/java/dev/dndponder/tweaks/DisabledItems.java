package dev.dndponder.tweaks;

import dev.dndponder.DnDTarget;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/** I cut some things from the original mod. They are listed below. */
public final class DisabledItems {
    private static final List<ResourceLocation> TOOL_IDS = List.of(
            dnd("handheld_saw"),
            dnd("handheld_drill"),
            dnd("gatling_breaker"));
    private static final List<ResourceLocation> BURNER_STOCK_IDS = List.of(dnd("burner"));

    private static volatile Set<Item> hidden;
    private static volatile Set<Item> inert;

    private DisabledItems() {}

    private static ResourceLocation dnd(String path) {
        return ResourceLocation.fromNamespaceAndPath(DnDTarget.DND_MOD_ID, path);
    }

    public static void register(IEventBus modBus) {
        if (!Tweaks.disableHandheldTools && !Tweaks.disableBurnerStock) return;
        modBus.addListener(DisabledItems::onBuildCreativeTabs);
        if (!Tweaks.disableHandheldTools) return;
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, DisabledItems::onLeftClickBlock);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, DisabledItems::onBreak);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, DisabledItems::onRightClickItem);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, DisabledItems::onRightClickBlock);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, DisabledItems::onAttack);
    }

    /** Hidden from creative here. Remove this if you want creative access to all. */
    public static boolean isHidden(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Set<Item> set = hidden;
        if (set == null) {
            List<ResourceLocation> ids = new ArrayList<>();
            if (Tweaks.disableHandheldTools) ids.addAll(TOOL_IDS);
            if (Tweaks.disableBurnerStock) ids.addAll(BURNER_STOCK_IDS);
            hidden = set = resolve(ids);
        }
        return set.contains(stack.getItem());
    }

    /** Overkill in retrospect */
    public static boolean isDisabled(ItemStack stack) {
        if (stack.isEmpty()) return false;
        Set<Item> set = inert;
        if (set == null) inert = set = Tweaks.disableHandheldTools ? resolve(TOOL_IDS) : Set.of();
        return set.contains(stack.getItem());
    }

    private static Set<Item> resolve(List<ResourceLocation> ids) {
        return ids.stream()
                .filter(BuiltInRegistries.ITEM::containsKey)
                .map(BuiltInRegistries.ITEM::get)
                .collect(Collectors.toUnmodifiableSet());
    }

    // --- creative tabs ---------------------------------------------------------------------------

    private static void onBuildCreativeTabs(BuildCreativeModeTabContentsEvent event) {
        List<ItemStack> toRemove = new ArrayList<>();
        for (ItemStack stack : event.getParentEntries()) if (isHidden(stack)) toRemove.add(stack);
        for (ItemStack stack : event.getSearchEntries()) if (isHidden(stack) && !toRemove.contains(stack)) toRemove.add(stack);
        for (ItemStack stack : toRemove) {
            event.remove(stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    /** Overkill in retrospect */

    private static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (!isDisabled(event.getItemStack())) return;
        event.setCanceled(true);
        notify(event.getEntity());
    }

    private static void onBreak(BlockEvent.BreakEvent event) {
        if (!isDisabled(event.getPlayer().getMainHandItem())) return;
        event.setCanceled(true);
        notify(event.getPlayer());
    }

    private static void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (!isDisabled(event.getItemStack())) return;
        event.setCancellationResult(InteractionResult.FAIL);
        event.setCanceled(true);
        notify(event.getEntity());
    }

    private static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        if (!isDisabled(event.getItemStack())) return;
        event.setUseItem(TriState.FALSE);
    }

    private static void onAttack(AttackEntityEvent event) {
        if (!isDisabled(event.getEntity().getMainHandItem())) return;
        event.setCanceled(true);
        notify(event.getEntity());
    }

    private static void notify(Player player) {
        if (player.level().isClientSide) return;
        player.displayClientMessage(Component.translatableWithFallback(
                "dndesires_ponder.disabled_item", "This item is disabled.").withStyle(ChatFormatting.RED), true);
    }
}