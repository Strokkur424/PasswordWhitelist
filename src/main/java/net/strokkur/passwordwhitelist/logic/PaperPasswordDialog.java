package net.strokkur.passwordwhitelist.logic;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.input.DialogInput;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.dialog.DialogLike;
import net.kyori.adventure.key.Key;
import net.strokkur.passwordwhitelist.data.MainConfig;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@SuppressWarnings("UnstableApiUsage")
@NullMarked
public class PaperPasswordDialog implements PasswordDialog {

    private final DialogLike passwordDialog;

    public PaperPasswordDialog(MainConfig config) {
        passwordDialog = Dialog.create(builder -> builder.empty()
            .base(DialogBase.builder(config.dialog().title())
                .canCloseWithEscape(false)
                .body(config.dialog().extraText().stream().map(DialogBody::plainMessage).toList())
                .inputs(List.of(
                    DialogInput.text("password", config.dialog().label()).build()
                ))
                .build()
            )
            .type(DialogType.confirmation(
                ActionButton.builder(config.dialog().buttonPasswordAttempt())
                    .tooltip(config.dialog().buttonPasswordAttemptHover())
                    .action(DialogAction.customClick(Key.key("passwordwhitelist:response/attempt"), null))
                    .build(),
                ActionButton.builder(config.dialog().buttonAbort())
                    .tooltip(config.dialog().buttonAbortHover())
                    .action(DialogAction.customClick(Key.key("passwordwhitelist:response/abort"), null))
                    .build()
            ))
        );
    }

    @Override
    public void show(Audience audience) {
        audience.showDialog(passwordDialog);
    }
}
