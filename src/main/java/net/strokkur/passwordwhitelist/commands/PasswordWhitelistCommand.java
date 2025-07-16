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
package net.strokkur.passwordwhitelist.commands;

import com.destroystokyo.paper.profile.PlayerProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.strokkur.commands.annotations.Aliases;
import net.strokkur.commands.annotations.Command;
import net.strokkur.commands.annotations.Executes;
import net.strokkur.commands.annotations.Literal;
import net.strokkur.commands.annotations.Permission;
import net.strokkur.commands.annotations.arguments.StringArg;
import net.strokkur.passwordwhitelist.PasswordWhitelist;
import net.strokkur.passwordwhitelist.data.MessagesConfig;
import org.bukkit.command.CommandSender;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.util.UUID;

import static net.strokkur.commands.StringArgType.GREEDY;

@Command("passwordwhitelist")
@Aliases({"pw", "passw"})
@Permission("passwordwhitelist.command.use")
@NullMarked
class PasswordWhitelistCommand {

    private MessagesConfig messages() {
        return PasswordWhitelist.getInstance().getMessagesConfig();
    }

    @Executes
    void executeDefault(CommandSender sender) {
        executeHelp(sender);
    }

    @Executes("help")
    @Permission("passwordwhitelist.command.help")
    void executeHelp(CommandSender sender) {
        messages().help().main(sender, messages().tags(), messages().colors());
    }

    @Executes("help")
    @Permission("passwordwhitelist.command.help")
    void executeHelp(CommandSender sender, @Literal({"reload", "enable", "disable", "password", "attempts"}) String help) {
        switch (help) {
            case "reload" -> messages().help().reload(sender, messages().tags(), messages().colors());
            case "enable" -> messages().help().enable(sender, messages().tags(), messages().colors());
            case "disable" -> messages().help().disable(sender, messages().tags(), messages().colors());
            case "password" -> messages().help().password(sender, messages().tags(), messages().colors());
            case "attempts" -> messages().help().attempts(sender, messages().tags(), messages().colors());
        }
    }

    @Executes("reload")
    @Permission("passwordwhitelist.command.reload")
    void executeReload(CommandSender sender) {
        executeReload(sender, "all");
    }

    @Executes("reload")
    @Permission("passwordwhitelist.command.reload")
    void executeReload(CommandSender sender, @Literal({"all", "messages.yml", "config.yml", "data/password.properties", "data/failed_attempts.json"}) String config) {
        PasswordWhitelist plugin = PasswordWhitelist.getInstance();

        try {
            switch (config) {
                case "messages.yml" -> plugin.getMessagesConfig().reload(plugin);
                case "config.yml" -> {
                    plugin.getMainConfig().reload(plugin);
                    plugin.reloadDialogs();
                }
                case "data/password.properties" -> plugin.getPasswordManager().reload();
                case "data/failed_attempts.json" -> plugin.getFailedAttempts().reload();
                case "all" -> {
                    plugin.getMessagesConfig().reload(plugin);
                    plugin.getMainConfig().reload(plugin);
                    plugin.reloadDialogs();
                    plugin.getPasswordManager().reload();
                    plugin.getFailedAttempts().reload();
                }
            }

            sender.sendMessage(messages().reload(Placeholder.unparsed("config", config)));
        } catch (IOException exception) {
            sender.sendRichMessage("<red>A fatal exception occurred running this command. Please check the console for any stack traces.");
            plugin.getComponentLogger().error("An error occurred while reloading {}.", config, exception);
        }
    }

    @Executes("enable")
    @Permission("passwordwhitelist.command.enable")
    void executeEnable(CommandSender sender) {
        PasswordWhitelist plugin = PasswordWhitelist.getInstance();

        try {
            plugin.getPasswordManager().enablePassword();
            sender.sendMessage(messages().enable());
        } catch (IOException exception) {
            sender.sendRichMessage("<red>A fatal exception occurred running this command. Please check the console for any stack traces.");
            plugin.getComponentLogger().error("An error occurred enabling the password whitelist.", exception);
        }
    }

    @Executes("disable")
    @Permission("passwordwhitelist.command.disable")
    void executeDisable(CommandSender sender) {
        PasswordWhitelist plugin = PasswordWhitelist.getInstance();

        try {
            plugin.getPasswordManager().disablePassword();
            sender.sendMessage(messages().disable());
        } catch (IOException exception) {
            sender.sendRichMessage("<red>A fatal exception occurred running this command. Please check the console for any stack traces.");
            plugin.getComponentLogger().error("An error occurred disabling the password whitelist.", exception);
        }
    }

    @Executes("password")
    @Permission("passwordwhitelist.command.password")
    void executePasswordHelp(CommandSender sender) {
        messages().help().password(sender, messages().tags(), messages().colors());
    }

    @Executes("password set")
    @Permission("passwordwhitelist.command.password.set")
    void executePasswordSetHelp(CommandSender sender) {
        messages().help().passwordSet(sender, messages().tags(), messages().colors());
    }

    @Executes("password set")
    @Permission("passwordwhitelist.command.password.set")
    void executePasswordSet(CommandSender sender, @StringArg(GREEDY) String password) {
        PasswordWhitelist plugin = PasswordWhitelist.getInstance();

        try {
            plugin.getPasswordStore().setNewPassword(password);
            sender.sendMessage(messages().set(Placeholder.unparsed("password", password)));
        } catch (IOException exception) {
            sender.sendRichMessage("<red>A fatal exception occurred running this command. Please check the console for any stack traces.");
            plugin.getComponentLogger().error("An error occurred setting a new password.", exception);
        }
    }

    @Executes("password show")
    @Permission("passwordwhitelist.command.password.show")
    void executePasswordShow(CommandSender sender) {
        PasswordWhitelist plugin = PasswordWhitelist.getInstance();

        String passwordString = plugin.getPasswordStore().getCurrentPassword();
        Component password;

        if (passwordString == null) {
            password = Component.text("<none>").decorate(TextDecoration.ITALIC);
        } else {
            password = Component.text(passwordString);
        }

        sender.sendMessage(messages().show(Placeholder.component("password", password)));
    }

    @Executes("attempts")
    @Permission("passwordwhitelist.command.attempts")
    void executeAttemptsHelp(CommandSender sender) {
        messages().help().attempts(sender, messages().tags(), messages().colors());
    }

    @Executes("attempts reset")
    @Permission("passwordwhitelist.command.attempts")
    void executeAttemptsResetHelp(CommandSender sender) {
        messages().help().attemptsReset(sender, messages().tags(), messages().colors());
    }

    @Executes("attempts reset")
    @Permission("passwordwhitelist.command.attempts")
    void executeAttemptsReset(CommandSender sender, PlayerProfile profile) {
        PasswordWhitelist plugin = PasswordWhitelist.getInstance();

        String name = profile.getName();
        UUID uuid = profile.getId();

        if (name == null || uuid == null) {
            sender.sendRichMessage("<red>Invalid player!");
            return;
        }

        try {
            plugin.getFailedAttempts().setFailedAttempts(uuid, 0);
            sender.sendMessage(plugin.getMessagesConfig().resetAttempts(
                Placeholder.unparsed("player", name)
            ));
        } catch (IOException ioException) {
            sender.sendRichMessage("<red>A fatal exception occurred running this command. Please check the console for any stack traces.");
            plugin.getComponentLogger().error("An error occurred while resetting {}'s attempt count.", name, ioException);
        }
    }
}