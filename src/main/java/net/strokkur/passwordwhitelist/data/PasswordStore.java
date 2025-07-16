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

/**
 * Represents a class which can manage a password value.
 */
@NullMarked
public interface PasswordStore {

    /**
     * Reload the password from disk.
     *
     * @throws IOException if an exception occurs while reading the file
     */
    void reload() throws IOException;

    /**
     * Get the current password.
     *
     * @return password, or {@code null} if none set
     */
    @Nullable
    String getCurrentPassword();

    /**
     * Check whether the input password is correct.
     *
     * @param input the input password
     * @return if the input matches the password, or {@code true} if no password is set
     */
    boolean checkPassword(String input);

    /**
     * Set a new password and write it to disk.
     *
     * @param password the new password, or {@code null} to unset the password
     * @throws IOException if an exception occurs while writing to the file
     */
    void setNewPassword(@Nullable String password) throws IOException;
}