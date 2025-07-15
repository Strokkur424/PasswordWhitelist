package net.strokkur.passwordwhitelist;

import org.bukkit.plugin.java.JavaPlugin;

public class PasswordWhitelist extends JavaPlugin {

    public static PasswordWhitelist getInstance() {
        return JavaPlugin.getPlugin(PasswordWhitelist.class);
    }
}
