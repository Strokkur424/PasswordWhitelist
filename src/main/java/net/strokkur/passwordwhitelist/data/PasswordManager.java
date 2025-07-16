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
     * Enable the password requirement for joining the server.
     * 
     * @throws IOException if an exception occurs while saving the file
     */
    default void enablePassword() throws IOException {
        setPasswordEnabled(true);
    }

    /**
     * Disable the password requirement for joining the server.
     * @throws IOException if an exception occurs while saving the file
     */
    default void disablePassword() throws IOException {
        setPasswordEnabled(false);
    }

    /**
     * Set the password requirement.
     * 
     * @param value whether the password should be required
     * @throws IOException if an exception occurs while saving the file
     */
    void setPasswordEnabled(boolean value) throws IOException;
}
