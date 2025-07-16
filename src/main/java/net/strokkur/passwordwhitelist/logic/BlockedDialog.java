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

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.body.PlainMessageDialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.strokkur.passwordwhitelist.data.MainConfig;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("UnstableApiUsage")
public class BlockedDialog implements OpenableDialog {

    private final Dialog dialog;

    public BlockedDialog(MainConfig config) {
        List<PlainMessageDialogBody> bodyLines = new ArrayList<>();
        for (Component line : config.blockedDialog().extraText()) {
            bodyLines.add(DialogBody.plainMessage(line, config.blockedDialog().contentWidth()));
        }

        dialog = Dialog.create(builder -> builder.empty()
            .base(DialogBase.builder(config.blockedDialog().title())
                .canCloseWithEscape(false)
                .body(bodyLines)
                .build()
            )
            .type(DialogType.notice(
                ActionButton.builder(config.blockedDialog().button())
                    .tooltip(config.blockedDialog().buttonHover())
                    .action(DialogAction.customClick(Key.key("passwordwhitelist:response/abort"), null))
                    .build()
            ))
        );
    }

    @Override
    public void show(Audience audience) {
        audience.showDialog(dialog);
    }
}
