package dev.dndponder.client;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ClientConfig {
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.BooleanValue WARN_ON_VERSION_MISMATCH;
    public static final ModConfigSpec.ConfigValue<String> DISMISSED_FOR_VERSION;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        WARN_ON_VERSION_MISMATCH = builder
                .comment("Show a chat message once per launch when the installed Create: Dreams n' Desires",
                        "is not the version this addon was built for.")
                .define("warnOnVersionMismatch", true);

        DISMISSED_FOR_VERSION = builder
                .comment("Set automatically by '/dndponder dismiss'. The mismatch warning stays quiet while",
                        "this D&D version is installed, and comes back when D&D changes again.")
                .define("dismissedForVersion", "");

        SPEC = builder.build();
    }

    private ClientConfig() {}
}