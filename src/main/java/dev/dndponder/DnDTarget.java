package dev.dndponder;

import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.Optional;

public final class DnDTarget {
    public static final String DND_MOD_ID = "dndesires";

    /** 2.3a-BETA compat ONLY at this time */
    private static final String FALLBACK_TESTED_VERSION = "2.3a-BETA";

    private DnDTarget() {}

    public static String testedVersion() {
        return ownProperty("dndesires_tested_version").orElse(FALLBACK_TESTED_VERSION);
    }

    public static Optional<String> downloadUrl() {
        return ownProperty("dndesires_download_url");
    }

    public static Optional<String> addonPageUrl() {
        return ownProperty("addon_page_url");
    }

    public static Optional<String> installedVersion() {
        return dndInfo().map(info -> info.getVersion().toString());
    }

    public static boolean isTestedVersion() {
        Optional<IModInfo> info = dndInfo();
        if (info.isEmpty()) return false;
        String tested = testedVersion();
        ArtifactVersion installed = info.get().getVersion();
        if (installed.toString().equalsIgnoreCase(tested)) return true;
        try {
            return new DefaultArtifactVersion(tested).compareTo(installed) == 0;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static Optional<IModInfo> ownInfo() {
        return ModList.get().getModContainerById(DnDPonder.MOD_ID).map(c -> c.getModInfo());
    }

    /** my safety net. I'll add this to a config so you don't have to see it soon */
    static void logStatus() {
        String tested = testedVersion();
        String installed = installedVersion().orElse("not installed");
        if (isTestedVersion()) {
            DnDPonder.LOGGER.info("Create: Dreams n' Desires {} detected, matches the version this addon was built for.", installed);
        } else {
            DnDPonder.LOGGER.warn("This addon was built and tested for Create: Dreams n' Desires {}, but {} is installed. "
                    + "Ponder scenes and KubeJS schemas may not match the installed version.", tested, installed);
        }
    }

    private static Optional<IModInfo> dndInfo() {
        return ModList.get().getModContainerById(DND_MOD_ID).map(c -> c.getModInfo());
    }

    private static Optional<String> ownProperty(String key) {
        return ownInfo()
                .map(info -> info.getModProperties().get(key))
                .map(Object::toString)
                .map(String::trim)
                .filter(s -> !s.isEmpty());
    }
}