package com.example.Intellibus.models.sentiment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request model for sentiment analysis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentAnalysisRequest {
    /**
     * The text to analyze for sentiment
     */
    private String text;
    
    /**
     * Optional context information about the text
     */
    private String context;
    
    /**
     * Optional source of the text (e.g., "customer_feedback", "social_media", "chat")
     */
    private String source;
} 