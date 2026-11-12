package dev.dndponder.tweaks;

import dev.dndponder.DnDPonder;
import net.neoforged.neoforge.common.ModConfigSpec;

// Tweaks switchable in config/dndesires_ponder-startup.toml.
// Restart req
public final class Tweaks {
    public static final ModConfigSpec SPEC;
    private static final ModConfigSpec.BooleanValue DISABLE_HANDHELD_TOOLS_VALUE;
    private static final ModConfigSpec.BooleanValue DISABLE_BURNER_STOCK_VALUE;
    private static final ModConfigSpec.BooleanValue MILKSHAKE_RECIPES_VALUE;
    private static final ModConfigSpec.BooleanValue BRECCIA_QUARTZ_VALUE;
    private static final ModConfigSpec.BooleanValue REMOVE_MILKSHAKE_ORE_STONE_VALUE;
    private static final ModConfigSpec.BooleanValue MIXER_RECIPES_VALUE;
    private static final ModConfigSpec.BooleanValue DYEABLE_FAN_VALUE;
    private static final ModConfigSpec.BooleanValue DYEABLE_FAN_ANY_VERSION_VALUE;

    static {
        ModConfigSpec.Builder b = new ModConfigSpec.Builder();
        b.comment("Gameplay tweaks for Create: Dreams n' Desires. Read once at startup: restart to apply changes.",
                "Recipes are passed down server-side. The creative-tab hiding uses the client's copy.")
                .push("tweaks");

        DISABLE_HANDHELD_TOOLS_VALUE = b
                .comment("Remove the Handheld Saw, Handheld Drill and Gatling Breaker: no recipes, hidden from",
                        "creative and JEI, and existing copies are disabled.")
                .define("disableHandheldTools", true);

        DISABLE_BURNER_STOCK_VALUE = b
                .comment("Remove Burner Stock: its recipe and its smelting/blasting/smoking 'infinite fuel' recipes,",
                        "and hide it from creative and JEI. The Seething Catalyst Sail is crafted with a Blaze Burner",
                        "when this is enabled.")
                .define("disableBurnerStock", true);

        MILKSHAKE_RECIPES_VALUE = b
                .comment("Replaces the chocolate, vanilla and strawberry milkshake mixing recipes:",
                        "chocolate: milk instead of liquid chocolate; accepts Create or Pam's chocolate bars",
                        "vanilla: white flowers instead of blue orchids; Pam's vanilla bean when Pam's Trees is installed",
                        "strawberry: Pam's strawberries instead of sweet berries when Pam's Crops is installed")
                .define("milkshakeRecipes", true);

        // pumpkins really lol
        BRECCIA_QUARTZ_VALUE = b
                .comment("Breccia Hydraulic Compacting uses quartz instead of pumpkins.")
                .define("brecciaQuartz", true);

        REMOVE_MILKSHAKE_ORE_STONE_VALUE = b
                .comment("Milkshake + lava recipes for hydraulic compressor and bedrock crafting removed entirely.")
                .define("removeMilkshakeOreStone", true);

        MIXER_RECIPES_VALUE = b
                .comment("Add Mechanical Mixer recipes: dyeing asphalt and bore blocks for all colors",
                         "coal into coal pieces (gravel is used as an abrasive and returned).")
                .define("mixerRecipes", true);

        DYEABLE_FAN_VALUE = b
                .comment("Right-click an Industrial Fan with any dye to color its blades. Needs this addon enabled",
                        "on the server to dye and save colors, clients can install optionally.")
                .define("dyeableIndustrialFan", true);

        DYEABLE_FAN_ANY_VERSION_VALUE = b
                .comment("Safeguard for people who might update DD and forget about this mod.",
                        "I highly recommend against setting this to true.")
                .define("dyeableFanOnAnyDnDVersion", false);

        b.pop();
        SPEC = b.build();
    }

    public static boolean disableHandheldTools = true;
    public static boolean disableBurnerStock = true;
    public static boolean milkshakeRecipes = true;
    public static boolean brecciaQuartz = true;
    public static boolean removeMilkshakeOreStone = true;
    public static boolean mixerRecipes = true;
    public static boolean dyeableIndustrialFan = true;
    public static boolean dyeableFanOnAnyDnDVersion = false;

    private Tweaks() {}

    public static void load() {
        try {
            disableHandheldTools = DISABLE_HANDHELD_TOOLS_VALUE.get();
            disableBurnerStock = DISABLE_BURNER_STOCK_VALUE.get();
            milkshakeRecipes = MILKSHAKE_RECIPES_VALUE.get();
            brecciaQuartz = BRECCIA_QUARTZ_VALUE.get();
            removeMilkshakeOreStone = REMOVE_MILKSHAKE_ORE_STONE_VALUE.get();
            mixerRecipes = MIXER_RECIPES_VALUE.get();
            dyeableIndustrialFan = DYEABLE_FAN_VALUE.get();
            dyeableFanOnAnyDnDVersion = DYEABLE_FAN_ANY_VERSION_VALUE.get();
        } catch (IllegalStateException e) {
            DnDPonder.LOGGER.warn("Startup config not loaded yet; using default tweak settings", e);
        }
        DnDPonder.LOGGER.info("D&D tweaks: disableHandheldTools={}, disableBurnerStock={}, milkshakeRecipes={}, brecciaQuartz={}, removeMilkshakeOreStone={}, mixerRecipes={}, dyeableIndustrialFan={}",
                disableHandheldTools, disableBurnerStock, milkshakeRecipes, brecciaQuartz, removeMilkshakeOreStone, mixerRecipes, dyeableIndustrialFan);
    }
}