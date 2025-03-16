package com.example.Intellibus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Intellibus.models.sentiment.SentimentAnalysisRequest;
import com.example.Intellibus.models.sentiment.SentimentAnalysisResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SentimentAnalysisService {

    @Autowired
    private ClaudeAIService claudeAIService;
    
    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Analyzes the sentiment of the provided text.
     * 
     * @param request The sentiment analysis request containing the text to analyze
     * @return A detailed sentiment analysis result
     */
    public SentimentAnalysisResult analyzeSentiment(SentimentAnalysisRequest request) {
        try {
            log.info("Analyzing sentiment for text: {}", request.getText().substring(0, Math.min(50, request.getText().length())) + "...");
            
            // Create a prompt for Claude that asks for structured sentiment analysis
            String prompt = "Analyze the sentiment of the following text. Respond with ONLY a JSON object containing sentiment analysis details.\n\n" +
                           "Text to analyze: \"" + request.getText() + "\"\n\n";
            
            // Add context if available
            if (request.getContext() != null && !request.getContext().isEmpty()) {
                prompt += "Context: " + request.getContext() + "\n\n";
            }
            
            // Add source if available
            if (request.getSource() != null && !request.getSource().isEmpty()) {
                prompt += "Source: " + request.getSource() + "\n\n";
            }
            
            prompt += "Remember to respond with ONLY a JSON object that has these exact fields:\n" +
                     "- sentiment: overall sentiment (POSITIVE, NEGATIVE, NEUTRAL, or MIXED)\n" +
                     "- score: a decimal score from -1.0 (extremely negative) to 1.0 (extremely positive)\n" +
                     "- confidence: a decimal between 0.0 and 1.0 indicating your confidence\n" +
                     "- dominant_emotions: array of emotions detected\n" +
                     "- key_phrases: array of notable phrases\n" +
                     "- insights: brief textual explanation";
            
            // Call Claude API
            String responseText = claudeAIService.generateResponse(prompt);
            
            // Log the raw response for debugging
            log.debug("Raw Claude response: {}", responseText);
            
            // Extract the JSON from the response (in case Claude adds any text before/after the JSON)
            String jsonStr = extractJsonFromResponse(responseText);
            
            // Log the extracted JSON for debugging
            log.debug("Extracted JSON: {}", jsonStr);
            
            // Parse the JSON response
            SentimentAnalysisResult result = objectMapper.readValue(jsonStr, SentimentAnalysisResult.class);
            
            // Set the original text for reference
            result.setOriginalText(request.getText());
            
            log.info("Sentiment analysis complete. Result: {}", result.getSentiment());
            return result;
            
        } catch (Exception e) {
            log.error("Error analyzing sentiment: {}", e.getMessage(), e);
            SentimentAnalysisResult errorResult = new SentimentAnalysisResult();
            errorResult.setSentiment("ERROR");
            errorResult.setInsights("Error analyzing sentiment: " + e.getMessage());
            return errorResult;
        }
    }
    
    /**
     * Batch analyze sentiment for multiple texts.
     * 
     * @param requests List of sentiment analysis requests
     * @return List of sentiment analysis results
     */
    public SentimentAnalysisResult[] batchAnalyzeSentiment(SentimentAnalysisRequest[] requests) {
        SentimentAnalysisResult[] results = new SentimentAnalysisResult[requests.length];
        
        for (int i = 0; i < requests.length; i++) {
            results[i] = analyzeSentiment(requests[i]);
        }
        
        return results;
    }
    
    /**
     * Extract JSON object from Claude's response text, which might contain additional text.
     * 
     * @param responseText The full response from Claude
     * @return Just the JSON part of the response
     */
    private String extractJsonFromResponse(String responseText) {
        // Look for text that appears to be JSON (between curly braces)
        int startIdx = responseText.indexOf('{');
        int endIdx = responseText.lastIndexOf('}') + 1;
        
        if (startIdx >= 0 && endIdx > startIdx) {
            return responseText.substring(startIdx, endIdx);
        }
        
        // If no JSON found, return the original text (will likely cause an error during parsing)
        return responseText;
    }
} 