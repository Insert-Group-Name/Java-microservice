package com.example.Intellibus.models.chat;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for chat conversation analysis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatAnalysisRequest {
    
    /**
     * List of chat messages in the conversation
     */
    private List<ChatMessage> messages;
    
    /**
     * Optional context for the chat (customer service, sales, etc.)
     */
    private String context;
    
    /**
     * Optional user ID or identifier
     */
    private String userId;
    
    /**
     * Inner class representing a single chat message
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessage {
        /**
         * Who sent the message (e.g., "user", "agent", "system")
         */
        private String role;
        
        /**
         * The message content
         */
        private String content;
        
        /**
         * Optional timestamp for the message
         */
        private String timestamp;
    }
} 