package com.example.messenger.webSocket.dto;

public class MessageReadEvent extends Event{
    private long memberId;
    private long lastReadMessageId;

    public MessageReadEvent(long memberId, long lastReadMessageId) {
        super(EventType.MESSAGE_READ);
        this.memberId = memberId;
        this.lastReadMessageId = lastReadMessageId;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getLastReadMessageId() {
        return lastReadMessageId;
    }

    public void setLastReadMessageId(long lastReadMessageId) {
        this.lastReadMessageId = lastReadMessageId;
    }
}
