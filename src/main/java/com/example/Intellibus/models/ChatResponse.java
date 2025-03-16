package com.example.Intellibus.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {
    private String response;
    private String model;
    private String requestId;
    private long processingTimeMs;
    private long apiCallTimeMs;
    
    // Constructor compatible with existing code
    public ChatResponse(String response, String model) {
        this.response = response;
        this.model = model;
    }
} 