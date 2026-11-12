package dev.dndponder.tweaks;

import dev.dndponder.DnDPonder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.event.AddPackFindersEvent;

public final class TweakPacks {
    private TweakPacks() {}

    public static void onAddPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() != PackType.SERVER_DATA) return;

        // KubeJS recipe schemas. They optionally inherit from KubeJS Create
        if (ModList.get().isLoaded("kubejs_create")) {
            register(event, "compat/kubejs_create", "D&D KubeJS Create schemas");
        }

        if (Tweaks.disableHandheldTools) add(event, "disable_handheld_tools", "Disable D&D handheld tools");
        if (Tweaks.disableBurnerStock) add(event, "disable_burner_stock", "Disable D&D Burner Stock");
        if (Tweaks.milkshakeRecipes) add(event, "milkshakes", "D&D milkshake recipes");
        if (Tweaks.brecciaQuartz) add(event, "breccia_quartz", "D&D breccia from quartz");
        if (Tweaks.removeMilkshakeOreStone) add(event, "no_milkshake_ore_stone", "No D&D milkshake ore stones");
        if (Tweaks.mixerRecipes) add(event, "mixer_recipes", "D&D mixer recipes");
    }

    private static void add(AddPackFindersEvent event, String name, String title) {
        register(event, "tweaks/" + name, title);
    }

    private static void register(AddPackFindersEvent event, String path, String title) {
        try {
            event.addPackFinders(
                    ResourceLocation.fromNamespaceAndPath(DnDPonder.MOD_ID, path),
                    PackType.SERVER_DATA,
                    Component.literal(title),
                    PackSource.BUILT_IN,
                    true,
                    Pack.Position.TOP);
        } catch (RuntimeException e) {
            DnDPonder.LOGGER.error("Could not register built-in datapack '{}'", path, e);
        }
    }
}