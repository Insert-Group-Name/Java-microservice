package com.example.Intellibus.models.sentiment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result model for sentiment analysis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SentimentAnalysisResult {
    /**
     * Overall sentiment (POSITIVE, NEGATIVE, NEUTRAL, MIXED, or ERROR)
     */
    private String sentiment;
    
    /**
     * Sentiment score from -1.0 (extremely negative) to 1.0 (extremely positive)
     */
    private double score;
    
    /**
     * Confidence level in the sentiment analysis (0.0 to 1.0)
     */
    private double confidence;
    
    /**
     * List of dominant emotions detected in the text
     */
    @JsonProperty("dominant_emotions")
    private List<String> dominantEmotions;
    
    /**
     * Key phrases that influenced the sentiment analysis
     */
    @JsonProperty("key_phrases")
    private List<String> keyPhrases;
    
    /**
     * Detailed insights about the sentiment analysis
     */
    private String insights;
    
    /**
     * Original text that was analyzed (optional)
     */
    private String originalText;
    
    /**
     * Request ID for tracking purposes
     */
    private String requestId;
    
    /**
     * Processing time in milliseconds
     */
    private long processingTimeMs;
} 