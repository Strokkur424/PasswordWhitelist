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
package net.strokkur.passwordwhitelist.data;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.strokkur.config.Format;
import net.strokkur.config.annotations.ConfigFilePath;
import net.strokkur.config.annotations.ConfigFormat;
import net.strokkur.config.annotations.CustomParse;
import net.strokkur.config.annotations.GenerateConfig;
import org.jspecify.annotations.NullUnmarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@GenerateConfig
@ConfigFilePath("messages.yml")
@ConfigFormat(Format.YAML_CONFIGURATE)
@NullUnmarked
@ConfigSerializable
class MessagesConfigModel {

    Map<String, String> colors;

    Map<String, String> tags;

    @CustomParse("parseWithPrefix")
    String reload;

    @CustomParse("parseWithPrefix")
    String enable;

    @CustomParse("parseWithPrefix")
    String disable;

    @CustomParse("parseWithPrefix")
    String set;

    @CustomParse("parseWithPrefix")
    String show;

    @CustomParse("parseWithPrefix")
    String resetAttempts;

    @CustomParse("parseWithPrefix")
    String passwordIncorrect;

    @CustomParse("parseWithPrefix")
    String disconnect;

    Help help;

    private static Component parseStringWithPrefix(String message, Map<String, String> tags, Map<String, String> colors, TagResolver... resolvers) {
        MiniMessage mm = MiniMessage.miniMessage();

        List<TagResolver> extraResolvers = new ArrayList<>(colors.size() + tags.size());
        for (Map.Entry<String, String> declaredColor : colors.entrySet()) {
            @SuppressWarnings("PatternValidation")
            TagResolver res = TagResolver.resolver(
                declaredColor.getKey(),
                Tag.inserting(mm.deserialize(declaredColor.getValue()))
            );

            extraResolvers.add(res);
        }

        for (Map.Entry<String, String> declaredTag : tags.entrySet()) {
            @SuppressWarnings("PatternValidation")
            TagResolver res = TagResolver.resolver(
                declaredTag.getKey(),
                Tag.preProcessParsed(declaredTag.getValue())
            );

            extraResolvers.add(res);
        }

        return mm.deserialize(message, TagResolver.resolver(
            TagResolver.resolver(resolvers),
            TagResolver.resolver(extraResolvers)
        ));
    }

    public Component parseWithPrefix(String message, TagResolver... resolvers) {
        return parseStringWithPrefix(message, this.tags, this.colors, resolvers);
    }

    @ConfigSerializable
    static class Help {

        @CustomParse("sendToSender")
        String main;

        @CustomParse("sendToSender")
        String reload;

        @CustomParse("sendToSender")
        String enable;

        @CustomParse("sendToSender")
        String disable;

        @CustomParse("sendToSender")
        String password;

        @CustomParse("sendToSender")
        String passwordSet;

        @CustomParse("sendToSender")
        String attempts;

        @CustomParse("sendToSender")
        String attemptsReset;

        String sendToSender(String message, Audience audience, Map<String, String> tags, Map<String, String> colors, TagResolver... resolvers) {
            audience.sendMessage(parseStringWithPrefix(message, tags, colors, resolvers));
            return message;
        }
    }
}
