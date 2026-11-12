package dev.dndponder.client.ponder;

import dev.dndponder.DnDPonder;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.scene.PonderStoryBoard;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

import static dev.dndponder.client.ponder.DnDIds.dnd;

public final class DnDPonderPlugin implements PonderPlugin {
    private static final String[] COLORS = {"white", "orange", "magenta", "light_blue", "yellow", "lime", "pink",
            "gray", "light_gray", "cyan", "purple", "blue", "brown", "green", "red", "black"};

    @Override
    public String getModId() {
        return DnDPonder.MOD_ID;
    }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        // --- Scenes on Create's own schematics ---
        scene(helper, DnDIds.CREATE_COMPACTING_SCHEMATIC, HydraulicPressScenes::basics,
                List.of(DnDIds.HYDRAULIC_PRESS), DnDIds.HYDRAULIC_PRESS);
        scene(helper, DnDIds.CREATE_FAN_PROCESSING_SCHEMATIC, FanCatalystScenes::catalysts,
                List.of(DnDIds.ENCASED_FAN));
        scene(helper, DnDIds.CREATE_FAN_PROCESSING_SCHEMATIC, CatalystSailScenes::sails,
                DnDIds.ALL_CATALYST_SAILS,
                CatalystSailScenes.SPLASHING, CatalystSailScenes.SMOKING, CatalystSailScenes.BLASTING, CatalystSailScenes.HAUNTING);

        scene(helper, ShowcaseUtil.BASE_5, MachineScenes::stirlingEngine,
                List.of(dnd("stirling_engine")), dnd("stirling_engine"), dnd("powered_flywheel"));
        scene(helper, ShowcaseUtil.BASE_5, MachineScenes::industrialFan,
                List.of(dnd("industrial_fan")), dnd("industrial_fan"));
        if (dev.dndponder.tweaks.DyeableFan.isActive()) {
            scene(helper, ShowcaseUtil.BASE_5, MachineScenes::industrialFanDyeing,
                    List.of(dnd("industrial_fan")), dnd("industrial_fan"));
        }
        scene(helper, ShowcaseUtil.BASE_5, MachineScenes::cogCranks,
                List.of(dnd("cog_crank"), dnd("large_cog_crank")), dnd("cog_crank"), dnd("large_cog_crank"));
        scene(helper, ShowcaseUtil.BASE_5, MachineScenes::inverseGearshift,
                List.of(dnd("inverse_gearshift")), dnd("inverse_gearshift"));
        scene(helper, ShowcaseUtil.BASE_5, MachineScenes::omniGearbox,
                List.of(dnd("omni_gearbox")), dnd("omni_gearbox"));
        scene(helper, ShowcaseUtil.BASE_5, MachineScenes::omniSpeedController,
                List.of(dnd("omni_speed_controller")), dnd("omni_speed_controller"));
        scene(helper, ShowcaseUtil.BASE_5, MachineScenes::multimeter,
                List.of(dnd("multimeter")), dnd("multimeter"));
        scene(helper, ShowcaseUtil.BASE_5, MachineScenes::goldenMixer,
                List.of(dnd("gold_mixer")), dnd("gold_mixer"));
        scene(helper, ShowcaseUtil.BASE_5, MachineScenes::spudSentry,
                List.of(dnd("spud_sentry")), dnd("spud_sentry"));
        scene(helper, ShowcaseUtil.BASE_5, MachineScenes::creativeGearMotor,
                List.of(dnd("creative_gear_motor")), dnd("creative_gear_motor"));

        scene(helper, ShowcaseUtil.BASE_5, BlockScenes::smartHopper,
                List.of(dnd("smart_hopper")), dnd("smart_hopper"));
        scene(helper, ShowcaseUtil.BASE_5, BlockScenes::fluidGaugeAndHatch,
                List.of(dnd("fluid_gauge"), dnd("fluid_hatch")), dnd("fluid_gauge"), dnd("fluid_hatch"));
        scene(helper, ResourceLocation.fromNamespaceAndPath("create", "item_drain"), BlockScenes::rollTable,
                List.of(dnd("roll_table")), dnd("roll_table"));
        scene(helper, ShowcaseUtil.BASE_5, BlockScenes::casings,
                List.of(dnd("industrial_casing"), dnd("overburden_casing")), dnd("industrial_casing"), dnd("overburden_casing"));
        scene(helper, ShowcaseUtil.BASE_5, BlockScenes::asphalt,
                withColors("asphalt"), dnd("asphalt"), dnd("black_asphalt"), dnd("red_asphalt"));
        scene(helper, ShowcaseUtil.BASE_5, BlockScenes::boreBlocks,
                withColors("bore_block"), dnd("bore_block"));
    }

    private static List<ResourceLocation> withColors(String base) {
        List<ResourceLocation> ids = new ArrayList<>();
        ids.add(dnd(base));
        for (String color : COLORS) ids.add(dnd(color + "_" + base));
        return ids;
    }

    private static void scene(PonderSceneRegistrationHelper<ResourceLocation> helper, ResourceLocation schematic,
                              PonderStoryBoard storyBoard, List<ResourceLocation> components,
                              ResourceLocation... requiredBlocks) {
        for (ResourceLocation block : requiredBlocks) {
            if (!DnDIds.hasBlock(block)) {
                DnDPonder.LOGGER.warn("{} not found in this Dreams n' Desires version, skipping a Ponder scene", block);
                return;
            }
        }
        List<ResourceLocation> present = new ArrayList<>();
        for (ResourceLocation id : components) {
            if (DnDIds.hasItem(id)) present.add(id);
            else DnDPonder.LOGGER.debug("{} has no item in this Dreams n' Desires version, not attaching a Ponder scene to it", id);
        }
        if (present.isEmpty()) return;
        helper.forComponents(present).addStoryBoard(schematic, storyBoard);
    }
}