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

import java.io.IOException;
import java.util.UUID;

/**
 * Represent a class which holds the amount of failed password attempts since the last login.
 */
public interface FailedAttemptsStore {

    /**
     * Reload the failed attempts from disk.
     *
     * @throws IOException if an exception occurs while reading the file
     */
    void reload() throws IOException;

    /**
     * Increment the number of failed attempts by one.
     *
     * @param uuid uuid of the connecting player
     * @throws IOException if an exception occurs while saving the data to disk
     */
    default void incrementFailedAttempts(UUID uuid) throws IOException {
        setFailedAttempts(uuid, getFailedAttempts(uuid) + 1);
    }

    /**
     * Set the number of failed attempts
     *
     * @param uuid  uuid of the connecting player
     * @param value the new number of failed attempts
     * @throws IOException if an exception occurs while saving the data to disk
     */
    void setFailedAttempts(UUID uuid, int value) throws IOException;

    /**
     * Get the number of failed attempts
     *
     * @param uuid uuid of the connecting player
     * @return number of failed attempts
     */
    int getFailedAttempts(UUID uuid);
}
