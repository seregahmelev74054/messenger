package com.example.messenger.MEMBER.controller;

import com.example.messenger.MEMBER.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PatchMapping("/api/v1/conversations/{conversationId}/read/{lastReadMessageId}")
    public ResponseEntity<Void> patchLastReadMessage(
            @PathVariable long conversationId,
            @PathVariable long lastReadMessageId
    ) {

        memberService.patchLastReadMessageId(conversationId, lastReadMessageId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
