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

/**
 * Represents a class for enabling/disabling the password feature.
 */
public interface PasswordManager {

    /**
     * Reload the enabled status from disk.
     *
     * @throws IOException if an exception occurs while reading the file
     */
    void reload() throws IOException;

    /**
     * Get whether a password is required to join the server.
     *
     * @return whether a password is required
     */
    boolean isPasswordEnabled();
    /**
     * Set the password requirement.
     *
     * @param value whether the password should be required
     * @throws IOException if an exception occurs while saving the file
     */
    void setPasswordEnabled(boolean value) throws IOException;
    /**
     * Enable the password requirement for joining the server.
     *
     * @throws IOException if an exception occurs while saving the file
     */
    default void enablePassword() throws IOException {
        setPasswordEnabled(true);
    }
    /**
     * Disable the password requirement for joining the server.
     *
     * @throws IOException if an exception occurs while saving the file
     */
    default void disablePassword() throws IOException {
        setPasswordEnabled(false);
    }
}
