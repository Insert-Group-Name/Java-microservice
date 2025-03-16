package com.example.Intellibus.models.chat;

import java.util.List;
import java.util.Map;

import com.example.Intellibus.models.sentiment.SentimentAnalysisResult;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Result model for chat conversation analysis
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatAnalysisResult {
    
    /**
     * Overall sentiment of the conversation
     */
    private SentimentAnalysisResult overallSentiment;
    
    /**
     * Main topics discussed in the conversation
     */
    private List<String> mainTopics;
    
    /**
     * User intent identified from the conversation
     */
    private List<String> userIntents;
    
    /**
     * Key questions asked in the conversation
     */
    private List<String> keyQuestions;
    
    /**
     * Any issues or problems identified in the conversation
     */
    private List<String> identifiedIssues;
    
    /**
     * Action items that could be extracted from the conversation
     */
    private List<String> actionItems;
    
    /**
     * Summary of the conversation
     */
    private String conversationSummary;
    
    /**
     * Map of specific message analytics for each message in the conversation
     * The key is the message index
     */
    private Map<Integer, MessageAnalytics> messageAnalytics;
    
    /**
     * Request ID for tracking
     */
    private String requestId;
    
    /**
     * Processing time in milliseconds
     */
    private long processingTimeMs;
    
    /**
     * Analytics for a specific message in the conversation
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageAnalytics {
        /**
         * Sentiment of this specific message
         */
        private SentimentAnalysisResult sentiment;
        
        /**
         * Topics discussed in this message
         */
        private List<String> topics;
        
        /**
         * Intent of this message
         */
        private String intent;
        
        /**
         * Importance of this message in the overall conversation (1-10)
         */
        private int importance;
        
        /**
         * Whether this message contains a question
         */
        private boolean containsQuestion;
    }
} 