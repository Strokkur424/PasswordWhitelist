package net.strokkur.passwordwhitelist.logic;

import io.papermc.paper.connection.PlayerCommonConnection;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigureEvent;
import io.papermc.paper.event.player.PlayerCustomClickEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.strokkur.passwordwhitelist.PasswordWhitelist;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Yes, this is taken from the PaperMC docs. That's fine - it's still my code :P
 */
@SuppressWarnings("UnstableApiUsage")
public class ServerJoinListener implements Listener {

    /**
     * A map for holding all currently connecting players.
     */
    private final Map<PlayerCommonConnection, CompletableFuture<Boolean>> awaitingResponse = new HashMap<>();

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
        CompletableFuture<Boolean> response = new CompletableFuture<>();

        // Put it into our map
        awaitingResponse.put(event.getConnection(), response);

        // Show the connecting player the dialog
        plugin.getPasswordDialog().show(event.getConnection().getAudience());

        // Wait until the future is complete. This set is necessary to keep the player in the configuration phase
        if (!response.join()) {
            // If the response is false, they declined. Therefore, we kick them from the server
            event.getConnection().disconnect(Component.text("Incorrect password!", NamedTextColor.RED));
        }

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
                setConnectionJoinResult(event.getCommonConnection(), false);
                return;
            }

            String password = response.getText("password");
            if (password == null) {
                setConnectionJoinResult(event.getCommonConnection(), false);
                return;
            }

            setConnectionJoinResult(
                event.getCommonConnection(),
                PasswordWhitelist.getInstance().getPasswordStore().checkPassword(password)
            );
            return;
        }

        if (key.equals(Key.key("passwordwhitelist:response/abort"))) {
            // If the identifier is the same as the disagree one, set the connection result to false
            setConnectionJoinResult(event.getCommonConnection(), false);
            return;
        }
    }

    /**
     * Simple utility method for setting a connection's dialog response result.
     */
    private void setConnectionJoinResult(PlayerCommonConnection connection, boolean value) {
        CompletableFuture<Boolean> future = awaitingResponse.get(connection);
        if (future != null) {
            future.complete(value);
        }
    }
}
