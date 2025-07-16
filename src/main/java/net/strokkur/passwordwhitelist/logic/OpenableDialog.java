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
