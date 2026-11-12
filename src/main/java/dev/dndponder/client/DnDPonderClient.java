package dev.dndponder.client;

import dev.dndponder.DnDPonder;
import dev.dndponder.client.data.DnDDatagen;
import dev.dndponder.client.ponder.DnDPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(value = DnDPonder.MOD_ID, dist = Dist.CLIENT)
public final class DnDPonderClient {

    public DnDPonderClient(IEventBus modBus, ModContainer container) {
        container.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC);
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);

        modBus.addListener(FMLClientSetupEvent.class,
                event -> event.enqueueWork(() -> PonderIndex.addPlugin(new DnDPonderPlugin())));

        modBus.addListener(GatherDataEvent.class, DnDDatagen::gatherData);

        DyedFanRenderer.init();
        modBus.addListener(DyedFanRenderer::onRegisterRenderers);
        NeoForge.EVENT_BUS.addListener(DyedFanRenderer::onClientTick);

        NeoForge.EVENT_BUS.addListener(VersionWarning::onLoggingIn);
        NeoForge.EVENT_BUS.addListener(VersionWarning::onLoggingOut);
        NeoForge.EVENT_BUS.addListener(VersionWarning::onClientTick);
        NeoForge.EVENT_BUS.addListener(VersionWarning::onRegisterClientCommands);
    }
}