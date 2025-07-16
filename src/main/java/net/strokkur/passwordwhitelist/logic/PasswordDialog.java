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
package net.strokkur.passwordwhitelist.logic;

import io.papermc.paper.connection.PlayerConfigurationConnection;
import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.body.PlainMessageDialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.strokkur.passwordwhitelist.data.FailedAttemptsStore;
import net.strokkur.passwordwhitelist.data.MainConfig;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class PasswordDialog implements OpenableDialog {

    private final MainConfig config;
    private final FailedAttemptsStore failedAttempts;

    public PasswordDialog(MainConfig config, FailedAttemptsStore failedAttempts) {
        this.config = config;
        this.failedAttempts = failedAttempts;
    }

    private DialogLike create(TagResolver... resolvers) {
        List<PlainMessageDialogBody> bodyLines = new ArrayList<>();
        for (Component line : config.passwordDialog().extraText(resolvers)) {
            bodyLines.add(DialogBody.plainMessage(line, config.passwordDialog().contentWidth()));
        }

        return Dialog.create(builder -> builder.empty()
            .base(DialogBase.builder(config.passwordDialog().title(resolvers))
                .canCloseWithEscape(false)
                .body(bodyLines)
                .inputs(List.of(
                    DialogInput.text("password", config.passwordDialog().label(resolvers)).build()
                ))
                .build()
            )
            .type(DialogType.confirmation(
                ActionButton.builder(config.passwordDialog().buttonPasswordAttempt(resolvers))
                    .tooltip(config.passwordDialog().buttonPasswordAttemptHover(resolvers))
                    .action(DialogAction.customClick(Key.key("passwordwhitelist:response/attempt"), null))
                    .build(),
                ActionButton.builder(config.passwordDialog().buttonAbort(resolvers))
                    .tooltip(config.passwordDialog().buttonAbortHover(resolvers))
                    .action(DialogAction.customClick(Key.key("passwordwhitelist:response/abort"), null))
                    .build()
            ))
        );
    }

    @Override
    public void show(Audience audience) {
        if (audience instanceof ForwardingAudience forwarding) {
            forwarding.forEachAudience(this::show);
            return;
        }

        int max = config.maxIncorrectPasswordAttempts();

        UUID uuid = null;
        if (audience instanceof Player player) {
            uuid = player.getUniqueId();
        } else if (audience instanceof PlayerConfigurationConnection conn) {
            uuid = conn.getProfile().getId();
        }

        int failed = uuid != null ? failedAttempts.getFailedAttempts(uuid) : 0;
        TagResolver resolvers = TagResolver.resolver(
            Placeholder.component("failed_attempts", Component.text(failed)),
            Placeholder.component("remaining_attempts", Component.text(max > 0 ? Integer.toString(Math.max(max - failed, 0)) : "infinite")),
            Placeholder.component("max_attempts", Component.text(max))
        );

        audience.showDialog(create(resolvers));
    }
}
