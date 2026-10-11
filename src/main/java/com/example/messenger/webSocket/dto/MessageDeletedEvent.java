package com.example.messenger.webSocket.dto;


public class MessageDeletedEvent extends Event {
    private MDType mdType;
    private long messageId;
    private Long memberId;

    public MessageDeletedEvent(MDType mdType, long messageId, Long memberId) {
        super(EventType.MESSAGE_DELETED);
        this.mdType = mdType;
        this.messageId = messageId;
        this.memberId = memberId;
    }

    public MDType getMdType() {
        return mdType;
    }

    public void setMdType(MDType mdType) {
        this.mdType = mdType;
    }

    public long getMessageId() {
        return messageId;
    }

    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }
}
