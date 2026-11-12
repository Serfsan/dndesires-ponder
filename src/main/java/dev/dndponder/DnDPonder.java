package dev.dndponder;

import com.mojang.logging.LogUtils;
import dev.dndponder.tweaks.DisabledItems;
import dev.dndponder.tweaks.DyeableFan;
import dev.dndponder.tweaks.TweakPacks;
import dev.dndponder.tweaks.Tweaks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.slf4j.Logger;

@Mod(DnDPonder.MOD_ID)
public final class DnDPonder {
    public static final String MOD_ID = "dndesires_ponder";
    public static final Logger LOGGER = LogUtils.getLogger();

    public DnDPonder(IEventBus modBus, ModContainer container) {
        // STARTUP configs are loaded by FML during this call, so the values are ready immediately.
        container.registerConfig(ModConfig.Type.STARTUP, Tweaks.SPEC);
        Tweaks.load();

        modBus.addListener(TweakPacks::onAddPackFinders);
        DisabledItems.register(modBus);
        DyeableFan.register(modBus);

        modBus.addListener(FMLCommonSetupEvent.class, event -> DnDTarget.logStatus());
    }
}