package net.strokkur.passwordwhitelist.logic;

import net.kyori.adventure.audience.Audience;

/**
 * Simple abstraction layer to mark a class which can open one, and only one, dialog
 * to an audience.
 */
public interface OpenableDialog {
    
    /**
     * Show the stored dialog to an audience.
     *
     * @param audience audience
     */
    void show(Audience audience);
}
