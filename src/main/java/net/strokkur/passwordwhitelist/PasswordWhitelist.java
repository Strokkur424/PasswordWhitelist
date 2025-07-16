package net.strokkur.passwordwhitelist;

import net.strokkur.passwordwhitelist.data.MessagesConfig;
import net.strokkur.passwordwhitelist.data.MessagesConfigImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.io.IOException;

public class PasswordWhitelist extends JavaPlugin {

    @MonotonicNonNull
    private MessagesConfig messagesConfig = null;

    private boolean disablePlugin = false;

    public static PasswordWhitelist getInstance() {
        return JavaPlugin.getPlugin(PasswordWhitelist.class);
    }

    @Override
    public void onLoad() {
        try {
            messagesConfig = new MessagesConfigImpl();
            messagesConfig.reload(this);
        } catch (IOException exception) {
            disablePlugin = true;
            getComponentLogger().error("A fatal exception has occurred. {} will disable itself...", getPluginMeta().getName(), exception);
        }
    }

    @Override
    public void onEnable() {
        if (disablePlugin) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }
}
