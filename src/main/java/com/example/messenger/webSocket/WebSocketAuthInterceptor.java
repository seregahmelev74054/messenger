package com.example.messenger.webSocket;

import com.example.messenger.exception.MemberNotFoundException;
import com.example.messenger.jwt.JwtService;
import com.example.messenger.member.entity.Member;
import com.example.messenger.member.entity.MemberStatus;
import com.example.messenger.member.repository.MemberRepository;
import io.jsonwebtoken.JwtException;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    public WebSocketAuthInterceptor(
            JwtService jwtService,
            MemberRepository memberRepository
    ) {
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && Objects.equals(accessor.getCommand(), StompCommand.CONNECT)) {

            String jwt = accessor.getFirstNativeHeader("Authorization");

            Map<String, Object> sessionAttributes =
                    accessor.getSessionAttributes();

            if (jwt != null && jwt.startsWith("Bearer ")) {
                jwt = jwt.substring(7);
            }
            else if (sessionAttributes != null) {
                jwt = (String) sessionAttributes.get("token");
            }

            if (jwt == null || jwt.isBlank()) {
                throw new BadCredentialsException("Connect must be authorized");
            }

            Long userId;

            try {
                userId = jwtService.extractUserId(jwt);
            } catch (JwtException e) {
                throw new BadCredentialsException("Invalid jwt");
            }

            if (userId == null)
                throw new BadCredentialsException("Invalid jwt");

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    Collections.emptyList()
            );

            accessor.setUser(authentication);

        }

        if (accessor != null && Objects.equals(accessor.getCommand(), StompCommand.SUBSCRIBE)) {

            String destination = accessor.getDestination();

            if (destination == null)
                throw new BadCredentialsException("");

            String prefix = "/topic/conversations/";

            Long conversationId = null;

            if (destination.startsWith(prefix)) {

                try {
                    conversationId = Long.parseLong(destination.substring(prefix.length()));
                }
                catch (NumberFormatException e) {
                    throw new BadCredentialsException("");
                }

                Authentication authentication = (Authentication) accessor.getUser();

                if (authentication == null)
                    throw new BadCredentialsException("Connect must be authorized");

                Long userId = (Long) authentication.getPrincipal();

                if (userId == null)
                    throw new BadCredentialsException("Connect must be authorized");

                Member member = memberRepository.findByUserIdAndConversationId(userId, conversationId)
                        .orElseThrow(() -> new MemberNotFoundException(""));

                if (member.getStatus() != MemberStatus.ACTIVE)
                    throw new MemberNotFoundException("");

            }

        }

        return message;

    }

}
