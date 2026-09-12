package com.example.messenger.MESSAGE.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateMessageRequest {

    @NotBlank
    @Size(min = 1, max = 2048)
    private String text;

    public String getText() {
        return text;
    }
}
