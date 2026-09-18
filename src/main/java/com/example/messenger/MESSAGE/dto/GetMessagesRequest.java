package com.example.messenger.MESSAGE.dto;

import java.time.Instant;
import java.util.Optional;

public class GetMessagesRequest {

    private Optional<Instant> lastCreatedAt;

    private Optional<Long> lastMessageId;

    public Optional<Long> getLastMessageId() {
        return lastMessageId;
    }

    public Optional<Instant> getLastCreatedAt() {
        return lastCreatedAt;
    }
}
