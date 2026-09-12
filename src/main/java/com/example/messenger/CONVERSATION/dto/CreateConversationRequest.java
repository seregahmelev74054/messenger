package com.example.messenger.CONVERSATION.dto;

import com.example.messenger.CONVERSATION.entity.ConversationType;
import com.example.messenger.CONVERSATION.entity.ConversationVisibility;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Optional;

public class CreateConversationRequest {

    @NotBlank(message = "the title shouldn't be empty")
    @Size(min = 1, max = 16)
    private String title;

    @NotNull(message = "the type is required")
    private ConversationType type;

    @NotNull(message = "the visibility is required")
    private ConversationVisibility visibility;

    private Optional<Long> companionId;

    public String getTitle() {
        return title;
    }

    public ConversationType getType() {
        return type;
    }

    public ConversationVisibility getVisibility() {
        return visibility;
    }

    public Optional<Long> getCompanionId() {
        return companionId;
    }
}
