package dev.dndponder.client;

import com.mojang.brigadier.Command;
import dev.dndponder.DnDPonder;
import dev.dndponder.DnDTarget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;

public final class VersionWarning {
    private static final int DELAY_TICKS = 40;
    private static boolean shown;
    private static int countdown = -1;

    private VersionWarning() {}

    static void onLoggingIn(ClientPlayerNetworkEvent.LoggingIn event) {
        if (!shown) countdown = DELAY_TICKS;
    }

    static void onLoggingOut(ClientPlayerNetworkEvent.LoggingOut event) {
        countdown = -1;
    }

    static void onClientTick(ClientTickEvent.Post event) {
        if (countdown < 0 || Minecraft.getInstance().player == null || countdown-- > 0) return;
        countdown = -1;
        shown = true;
        if (shouldWarn()) send(warning());
    }

    static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register(Commands.literal("dndponder").then(Commands.literal("dismiss").executes(ctx -> {
            String installed = DnDTarget.installedVersion().orElse("");
            ClientConfig.DISMISSED_FOR_VERSION.set(installed);
            ClientConfig.DISMISSED_FOR_VERSION.save();
            send(prefix().append(Component.literal("Got it. No more warnings while D&D " + installed + " is installed.")
                    .withStyle(ChatFormatting.GRAY)));
            return Command.SINGLE_SUCCESS;
        })));
    }

    private static boolean shouldWarn() {
        if (DnDTarget.isTestedVersion()) return false;
        try {
            return ClientConfig.WARN_ON_VERSION_MISMATCH.get()
                    && !ClientConfig.DISMISSED_FOR_VERSION.get().equalsIgnoreCase(DnDTarget.installedVersion().orElse(""));
        } catch (IllegalStateException configNotLoaded) {
            return true;
        }
    }

    private static Component warning() {
        String tested = DnDTarget.testedVersion();
        String installed = DnDTarget.installedVersion().orElse("?");
        MutableComponent message = prefix().append(Component.literal(
                "Built for Create: Dreams n' Desires " + tested + ", but you have " + installed
                        + ". Ponder scenes may not match your version. ").withStyle(ChatFormatting.YELLOW));
        DnDTarget.downloadUrl().ifPresent(url -> message.append(link("[Get D&D " + tested + "] ", ClickEvent.Action.OPEN_URL, url)));
        DnDTarget.addonPageUrl().ifPresent(url -> message.append(link("[Addon updates] ", ClickEvent.Action.OPEN_URL, url)));
        return message.append(link("[Don't warn me for " + installed + "]", ClickEvent.Action.RUN_COMMAND, "/dndponder dismiss"));
    }

    private static MutableComponent prefix() {
        return Component.literal("[D&D Ponder] ").withStyle(ChatFormatting.GOLD);
    }

    private static Component link(String text, ClickEvent.Action action, String value) {
        return Component.literal(text).withStyle(Style.EMPTY.withColor(ChatFormatting.AQUA).withUnderlined(true)
                .withClickEvent(new ClickEvent(action, value))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal(value))));
    }

    private static void send(Component message) {
        try {
            if (Minecraft.getInstance().player != null) Minecraft.getInstance().player.displayClientMessage(message, false);
        } catch (RuntimeException e) {
            DnDPonder.LOGGER.warn("Could not show the version message", e);
        }
    }
}