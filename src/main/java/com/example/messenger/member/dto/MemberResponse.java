package com.example.messenger.member.dto;

import com.example.messenger.member.entity.Member;

public class MemberResponse {

    public MemberResponse(Member member) {
        this.id = member.getId();
        this.displayName = member.getUser().getDisplayName();
    }

    public MemberResponse(long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
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
