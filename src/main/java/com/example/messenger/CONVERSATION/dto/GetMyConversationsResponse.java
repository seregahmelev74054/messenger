package com.example.messenger.CONVERSATION.dto;

import java.time.Instant;
import java.util.List;

public class GetMyConversationsResponse {

    private List<MyConversation> conversations;

    private Instant nextLastUpdatedAt;

    private Long nextLastId;


    public void setConversations(List<MyConversation> conversations) {
        this.conversations = conversations;
    }

    public void setNextLastUpdatedAt(Instant nextLastUpdatedAt) {
        this.nextLastUpdatedAt = nextLastUpdatedAt;
    }

    public void setNextLastId(Long nextLastId) {
        this.nextLastId = nextLastId;
    }

    public List<MyConversation> getConversations() {
        return conversations;
    }

    public Instant getNextLastUpdatedAt() {
        return nextLastUpdatedAt;
    }

    public Long getNextLastId() {
        return nextLastId;
    }
}
