package com.example.messenger.member.controller;

import com.example.messenger.member.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/api/v1/conversations/{conversationId}/invite/{addingUserId}")
    public ResponseEntity<Void> inviteMember(
            @PathVariable long conversationId,
            @PathVariable long addingUserId
    ) {

        memberService.inviteMember(addingUserId, conversationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/api/v1/conversations/{conversationId}/member")
    public ResponseEntity<Void> addMember(
            @PathVariable long conversationId
    ) {

        memberService.addMember(conversationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/api/v1/conversations/{conversationId}/member")
    public ResponseEntity<Void> leaveConversation(
            @PathVariable long conversationId
    ) {

        memberService.leaveConversation(conversationId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
