package com.thybak.bots.kkbot.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SecretionRankEntryTest {

    @Test
    void givenUsernameWithUnderscore_whenGetUsernameWithEscapedUnderscore_thenReturnEscapedUsername() {
        final var secretionRankEntry = new SecretionRankEntry("_underscored_username", 0L, 0L);
        assertEquals("\\_underscored\\_username", secretionRankEntry.getUsernameWithEscapedUnderscore());
    }

    @Test
    void givenUsernameWithoutUnderscore_whenGetUsernameWithEscapedUnderscore_thenReturnUsername() {
        final var secretionRankEntry = new SecretionRankEntry("username", 0L, 0L);
        assertEquals("username", secretionRankEntry.getUsernameWithEscapedUnderscore());
    }
}