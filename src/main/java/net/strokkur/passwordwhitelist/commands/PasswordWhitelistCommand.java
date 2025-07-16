package net.strokkur.passwordwhitelist.commands;

import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.strokkur.commands.StringArgType;
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
        messages().help().main(sender);
    }

    @Executes("reload")
    @Permission("passwordwhitelist.command.reload")
    void executeReload(CommandSender sender) {
        executeReload(sender, "all");
    }

    @Executes("reload")
    @Permission("passwordwhitelist.command.reload")
    void executeReload(CommandSender sender, @Literal({"all", "messages.yml"}) String config) {
        PasswordWhitelist plugin = PasswordWhitelist.getInstance();

        try {
            switch (config) {
                case "all", "messages.yml" -> plugin.getMessagesConfig().reload(plugin);
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
}