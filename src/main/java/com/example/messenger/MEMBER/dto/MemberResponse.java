package com.example.messenger.MEMBER.dto;

import com.example.messenger.MEMBER.entity.Member;

public class MemberResponse {

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.displayName = member.getUser().getDisplayName();
    }

    private long id;

    private String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public long getId() {
        return id;
    }
}
