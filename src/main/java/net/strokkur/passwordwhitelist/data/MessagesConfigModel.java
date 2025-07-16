package net.strokkur.passwordwhitelist.data;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.strokkur.config.Format;
import net.strokkur.config.annotations.ConfigFilePath;
import net.strokkur.config.annotations.ConfigFormat;
import net.strokkur.config.annotations.CustomParse;
import net.strokkur.config.annotations.GenerateConfig;
import org.jspecify.annotations.NullUnmarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@GenerateConfig
@ConfigFilePath("messages.yml")
@ConfigFormat(Format.YAML_CONFIGURATE)
@NullUnmarked
@ConfigSerializable
class MessagesConfigModel {
    
    String prefix;
    
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

    Help help;
    
    public Component parseWithPrefix(String message, TagResolver... resolvers) {
        return MiniMessage.miniMessage().deserialize(message, TagResolver.resolver(
            TagResolver.resolver(resolvers),
            Placeholder.parsed("prefix", prefix)
        ));
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

        String sendToSender(String message, Audience audience, TagResolver... resolvers) {
            audience.sendMessage(MiniMessage.miniMessage().deserialize(message, resolvers));
            return message;
        }
    }
}
