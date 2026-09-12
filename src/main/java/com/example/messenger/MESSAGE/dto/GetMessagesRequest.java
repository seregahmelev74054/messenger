package com.example.messenger.MESSAGE.dto;

import java.time.LocalDateTime;
import java.util.Optional;

public class GetMessagesRequest {

    private Optional<LocalDateTime> lastCreatedAt;

    private Optional<Long> lastMessageId;

    public Optional<Long> getLastMessageId() {
        return lastMessageId;
    }

    public Optional<LocalDateTime> getLastCreatedAt() {
        return lastCreatedAt;
    }
}
