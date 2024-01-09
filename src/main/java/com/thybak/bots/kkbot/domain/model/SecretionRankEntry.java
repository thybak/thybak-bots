package com.thybak.bots.kkbot.domain.model;

public record SecretionRankEntry(String username, Long poos, Long pukes) {
    public String getUsernameWithEscapedUnderscore() {
        return username.replace("_", "\\_");
    }
}
