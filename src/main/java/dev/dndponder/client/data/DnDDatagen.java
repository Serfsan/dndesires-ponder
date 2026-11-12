package dev.dndponder.client.data;

import dev.dndponder.DnDPonder;
import dev.dndponder.client.ponder.DnDPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

// this is for me the dev
// .\gradlew.bat runData changing ponder text
public final class DnDDatagen {
    private DnDDatagen() {}

    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeClient(), new Lang(event.getGenerator().getPackOutput()));
    }

    private static final class Lang extends LanguageProvider {
        Lang(PackOutput output) {
            super(output, DnDPonder.MOD_ID, "en_us");
        }

        @Override
        protected void addTranslations() {
            PonderIndex.addPlugin(new DnDPonderPlugin());
            PonderIndex.getLangAccess().provideLang(DnDPonder.MOD_ID, this::add);
        }
    }
}