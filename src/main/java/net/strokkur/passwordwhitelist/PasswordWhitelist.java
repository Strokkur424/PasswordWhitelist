/*
 * PasswordWhitelist - Set a global password for your server.
 * Copyright (C) 2025 Strokkur24
 *
 * You can redistribute this software under the terms of the
 * Creative Commons Attribution-NoDerivatives 4.0 International
 * license.
 *
 * You should have received a copy of the Creative Commons
 * Attribution-NoDerivatives 4.0 International license along
 * with this software; if not, see <https://creativecommons.org/licenses/by-nd/4.0/>.
 */
package net.strokkur.passwordwhitelist;

import net.strokkur.passwordwhitelist.data.BasicPasswordManager;
import net.strokkur.passwordwhitelist.data.FailedAttemptsStore;
import net.strokkur.passwordwhitelist.data.JsonFailedAttemptsStore;
import net.strokkur.passwordwhitelist.data.MainConfig;
import net.strokkur.passwordwhitelist.data.MainConfigImpl;
import net.strokkur.passwordwhitelist.data.MessagesConfig;
import net.strokkur.passwordwhitelist.data.MessagesConfigImpl;
import net.strokkur.passwordwhitelist.data.PasswordManager;
import net.strokkur.passwordwhitelist.data.PasswordStore;
import net.strokkur.passwordwhitelist.logic.BlockedDialog;
import net.strokkur.passwordwhitelist.logic.OpenableDialog;
import net.strokkur.passwordwhitelist.logic.PasswordDialog;
import net.strokkur.passwordwhitelist.logic.ServerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

public class PasswordWhitelist extends JavaPlugin {

    @MonotonicNonNull
    private MainConfig mainConfig = null;

    @MonotonicNonNull
    private MessagesConfig messagesConfig = null;

    @MonotonicNonNull
    private PasswordStore passwordStore = null;

    @MonotonicNonNull
    private PasswordManager passwordManager = null;

    @MonotonicNonNull
    private OpenableDialog passwordDialog = null;

    @MonotonicNonNull
    private OpenableDialog blockedDialog = null;

    @MonotonicNonNull
    private FailedAttemptsStore failedAttempts = null;

    private boolean disablePlugin = false;

    public static PasswordWhitelist getInstance() {
        return JavaPlugin.getPlugin(PasswordWhitelist.class);
    }

    @Override
    public void onLoad() {
        try {
            mainConfig = new MainConfigImpl();
            mainConfig.reload(this);

            messagesConfig = new MessagesConfigImpl();
            messagesConfig.reload(this);

            BasicPasswordManager basicPasswordManager = new BasicPasswordManager(this.getDataPath().resolve("data/password.properties"));
            passwordStore = basicPasswordManager;
            passwordManager = basicPasswordManager;

            failedAttempts = new JsonFailedAttemptsStore(this.getDataPath().resolve("data/failed_attempts.json"));
            failedAttempts.reload();

            basicPasswordManager.reload();
            reloadDialogs();
        } catch (Exception exception) {
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

        Bukkit.getPluginManager().registerEvents(new ServerJoinListener(), this);
    }

    public void reloadDialogs() {
        passwordDialog = new PasswordDialog(mainConfig, failedAttempts);
        blockedDialog = new BlockedDialog(mainConfig);
    }

    public MainConfig getMainConfig() {
        return mainConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public PasswordStore getPasswordStore() {
        return passwordStore;
    }

    public FailedAttemptsStore getFailedAttempts() {
        return failedAttempts;
    }

    public PasswordManager getPasswordManager() {
        return passwordManager;
    }

    public OpenableDialog getPasswordDialog() {
        return passwordDialog;
    }

    public OpenableDialog getBlockedDialog() {
        return blockedDialog;
    }
}
