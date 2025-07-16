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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JsonFailedAttemptsStore implements FailedAttemptsStore {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Map<UUID, Integer> failedAttempts = new HashMap<>();
    private final Path pathToFile;

    public JsonFailedAttemptsStore(Path pathToFile) {
        this.pathToFile = pathToFile;
    }

    @Override
    public void reload() throws IOException {
        if (!Files.exists(pathToFile)) {
            failedAttempts.clear();
            return;
        }

        String json = Files.readString(pathToFile, StandardCharsets.UTF_16);
        Map<UUID, Integer> map = GSON.fromJson(json, new TypeToken<>() {});

        failedAttempts.clear();
        failedAttempts.putAll(map);
    }

    @Override
    public void setFailedAttempts(UUID uuid, int value) throws IOException {
        if (value == 0) {
            failedAttempts.remove(uuid);
        } else {
            failedAttempts.put(uuid, value);
        }

        String json = GSON.toJson(failedAttempts);

        Files.createDirectories(pathToFile.getParent());
        Files.writeString(pathToFile, json, StandardCharsets.UTF_16);
    }

    @Override
    public int getFailedAttempts(UUID uuid) {
        return failedAttempts.getOrDefault(uuid, 0);
    }
}
