package net.strokkur.passwordwhitelist;

import net.strokkur.passwordwhitelist.data.BasicPasswordManager;
import net.strokkur.passwordwhitelist.data.MainConfig;
import net.strokkur.passwordwhitelist.data.MainConfigImpl;
import net.strokkur.passwordwhitelist.data.MessagesConfig;
import net.strokkur.passwordwhitelist.data.MessagesConfigImpl;
import net.strokkur.passwordwhitelist.data.PasswordManager;
import net.strokkur.passwordwhitelist.data.PasswordStore;
import net.strokkur.passwordwhitelist.logic.PaperPasswordDialog;
import net.strokkur.passwordwhitelist.logic.PasswordDialog;
import net.strokkur.passwordwhitelist.logic.ServerJoinListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.nullness.qual.MonotonicNonNull;

import java.io.IOException;

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
    private PasswordDialog passwordDialog = null;

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

            BasicPasswordManager basicPasswordManager = new BasicPasswordManager(this.getDataPath().resolve("password.properties"));
            passwordStore = basicPasswordManager;
            passwordManager = basicPasswordManager;

            basicPasswordManager.reload();
            reloadPasswordDialog();
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
    
    public void reloadPasswordDialog() {
        passwordDialog = new PaperPasswordDialog(mainConfig);
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

    public PasswordManager getPasswordManager() {
        return passwordManager;
    }
    
    public PasswordDialog getPasswordDialog() {
        return passwordDialog;
    }
}
