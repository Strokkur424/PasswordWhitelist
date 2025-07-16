package net.strokkur.passwordwhitelist.data;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
        properties.setProperty("password", password == null ? "" : password);
        properties.setProperty("enabled", Boolean.toString(passwordRequired));

        StringWriter writer = new StringWriter();
        properties.store(writer, "A file used by PasswordWhitelist to manage the current password and enable status.");

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
        
        this.password = properties.getProperty("password", "");
        if (this.password.isBlank()) {
            this.password = null;
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
