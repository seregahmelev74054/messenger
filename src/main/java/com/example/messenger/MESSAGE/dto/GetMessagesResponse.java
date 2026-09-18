package com.example.messenger.MESSAGE.dto;

import java.time.Instant;
import java.util.List;

public class GetMessagesResponse {

    public GetMessagesResponse(
            List<MessageResponse> messages,
            boolean hasMore,
            Instant nextLastCreatedAt,
            Long nextLastId) {
        this.messages = messages;
        this.hasMore = hasMore;
        this.nextLastCreatedAt = nextLastCreatedAt;
        this.nextLastId = nextLastId;
    }

    private List<MessageResponse> messages;

    private boolean hasMore;

    private Instant nextLastCreatedAt;

    private Long nextLastId;


    public List<MessageResponse> getMessages() {
        return messages;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public Instant getNextLastCreatedAt() {
        return nextLastCreatedAt;
    }

    public Long getNextLastId() {
        return nextLastId;
    }
}
