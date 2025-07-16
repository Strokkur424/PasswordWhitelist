package net.strokkur.passwordwhitelist.data;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.strokkur.config.Format;
import net.strokkur.config.annotations.ConfigFilePath;
import net.strokkur.config.annotations.ConfigFormat;
import net.strokkur.config.annotations.CustomParse;
import net.strokkur.config.annotations.GenerateConfig;
import org.jspecify.annotations.NullUnmarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@GenerateConfig
@ConfigFilePath("config.yml")
@ConfigFormat(Format.YAML_CONFIGURATE)
@NullUnmarked
@ConfigSerializable
class MainConfigModel {

    public Dialog dialog;

    @ConfigSerializable
    static class Dialog {

        @CustomParse("asComponent")
        String title;

        @CustomParse("asComponents")
        List<String> extraText;

        @CustomParse("asComponent")
        String label;
        
        @CustomParse("asComponent")
        String buttonPasswordAttempt;
        
        @CustomParse("asComponent")
        String buttonPasswordAttemptHover;
        
        @CustomParse("asComponent")
        String buttonAbort;
        
        @CustomParse("asComponent")
        String buttonAbortHover;

        Component asComponent(String value, TagResolver... resolvers) {
            return MiniMessage.miniMessage().deserialize(value, resolvers);
        }

        List<Component> asComponents(List<String> values, TagResolver... resolvers) {
            List<Component> out = new ArrayList<>(values.size());
            for (String value : values) {
                out.add(asComponent(value, resolvers));
            }
            
            return Collections.unmodifiableList(out);
        }
    }
}
