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

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Properties;

@NullMarked
public class BasicPasswordManager implements PasswordStore, PasswordManager {

    private final Path path;

    private @Nullable String password = null;
    private boolean passwordRequired = false;

    public BasicPasswordManager(Path path) {
        this.path = path;
    }

    private void save() throws IOException {
        Properties properties = new Properties();

        String base64password = "";
        if (password != null) {
            // The reason we are encoding the password to Base64 is not
            // for encryption (Base64 is not a suitable encryption algorithm),
            // but for UTF_16 charset support in the (forcefully)
            // ISO_8859_1 encoded properties file.
            byte[] bytes = password.getBytes(StandardCharsets.UTF_16);
            base64password = Base64.getEncoder().encodeToString(bytes);
        }

        properties.setProperty("password", base64password);
        properties.setProperty("enabled", Boolean.toString(passwordRequired));

        StringWriter writer = new StringWriter();
        properties.store(writer, """
            A file used by PasswordWhitelist to manage the current password and enable status.
            You should not manually edit this file and instead use the in-game commands.""");

        Files.createDirectories(path.getParent());
        Files.writeString(path, writer.toString(), StandardCharsets.ISO_8859_1);
    }

    @Override
    public void reload() throws IOException {
        if (!Files.exists(path)) {
            save();
            return;
        }

        String fileContent = Files.readString(path, StandardCharsets.ISO_8859_1);
        Properties properties = new Properties();
        properties.load(new StringReader(fileContent));

        String passwordString = properties.getProperty("password", "");
        if (passwordString.isBlank()) {
            this.password = null;
        } else {
            byte[] bytes = Base64.getDecoder().decode(passwordString);
            this.password = new String(bytes, StandardCharsets.UTF_16);
        }

        this.passwordRequired = Boolean.parseBoolean(properties.getProperty("enabled", "false"));
    }

    @Override
    public @Nullable String getCurrentPassword() {
        return password;
    }

    @Override
    public boolean checkPassword(String input) {
        return password == null || password.equals(input);
    }

    @Override
    public void setNewPassword(@Nullable String password) throws IOException {
        this.password = password;
        save();
    }

    @Override
    public boolean isPasswordEnabled() {
        return passwordRequired;
    }

    @Override
    public void setPasswordEnabled(boolean value) throws IOException {
        this.passwordRequired = value;
        save();
    }
}
