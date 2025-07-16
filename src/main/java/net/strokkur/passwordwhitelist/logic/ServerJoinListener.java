package net.strokkur.passwordwhitelist.logic;

import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import net.kyori.adventure.key.Key;
import net.strokkur.passwordwhitelist.PasswordWhitelist;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Yes, this is taken from the PaperMC docs. That's fine - it's still my code :P
 */
@SuppressWarnings("UnstableApiUsage")
public class ServerJoinListener implements Listener {

    /**
     * A map for holding all currently connecting players.
     */
    private final Map<PlayerCommonConnection, CompletableFuture<JoinResult>> awaitingResponse = new HashMap<>();

    @EventHandler
    void onPlayerConfigure(AsyncPlayerConnectionConfigureEvent event) {
        // Only show a password dialog if a password is required and set
        PasswordWhitelist plugin = PasswordWhitelist.getInstance();

        if (!plugin.getPasswordManager().isPasswordEnabled()) {
            return;
        }

        if (plugin.getPasswordStore().getCurrentPassword() == null) {
            plugin.getComponentLogger().warn("Password login is required, but no password is set. Please validate your password whitelist settings.");
            return;
        }

        // Construct a new completable future without a task
        CompletableFuture<JoinResult> response = new CompletableFuture<>();

        // Put it into our map
        awaitingResponse.put(event.getConnection(), response);

        // Show the connecting player the dialog
        plugin.getPasswordDialog().show(event.getConnection().getAudience());

        // Wait until the future is complete and then handle the result. This set is necessary to keep the player in the configuration phase.
        response.join().handle(event.getConnection());

        // We clean the map to avoid unnecessary entry buildup
        awaitingResponse.remove(event.getConnection());
    }

    /**
     * An event for handling dialog button click events.
     */
    @EventHandler
    void onHandleDialog(PlayerCustomClickEvent event) {
        Key key = event.getIdentifier();

        if (key.equals(Key.key("passwordwhitelist:response/attempt"))) {
            // If it is the same as the agree one, validate the password send alongside the request
            DialogResponseView response = event.getDialogResponseView();
            if (response == null) {
                setConnectionJoinResult(event.getCommonConnection(), JoinResult.DISCONNECT);
                return;
            }

            String password = response.getText("password");
            if (password == null) {
                setConnectionJoinResult(event.getCommonConnection(), JoinResult.DISCONNECT);
                return;
            }

            setConnectionJoinResult(
                event.getCommonConnection(),
                PasswordWhitelist.getInstance().getPasswordStore().checkPassword(password)
                    ? JoinResult.PERMITTED
                    : JoinResult.PASSWORD_INCORRECT
            );
            return;
        }

        if (key.equals(Key.key("passwordwhitelist:response/abort"))) {
            // If the identifier is the same as the disagree one, set the connection result to false
            setConnectionJoinResult(event.getCommonConnection(), JoinResult.DISCONNECT);
            return;
        }
    }

    /**
     * Simple utility method for setting a connection's dialog response result.
     */
    private void setConnectionJoinResult(PlayerCommonConnection connection, JoinResult value) {
        CompletableFuture<JoinResult> future = awaitingResponse.get(connection);
        if (future != null) {
            future.complete(value);
        }
    }

    private enum JoinResult {
        PERMITTED(e -> {}),
        PASSWORD_INCORRECT(e -> e.disconnect(PasswordWhitelist.getInstance().getMessagesConfig().passwordIncorrect())),
        DISCONNECT(e -> e.disconnect(PasswordWhitelist.getInstance().getMessagesConfig().disconnect()));

        private final Consumer<PlayerCommonConnection> handler;

        JoinResult(Consumer<PlayerCommonConnection> handler) {
            this.handler = handler;
        }

        public void handle(PlayerCommonConnection connection) {
            handler.accept(connection);
        }
    }
}
