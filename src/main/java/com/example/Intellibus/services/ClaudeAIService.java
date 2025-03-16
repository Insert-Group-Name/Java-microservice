package com.example.Intellibus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class ClaudeAIService {

    @Value("${spring.ai.anthropic.api-key}")
    private String apiKey;
    
    @Value("${spring.ai.anthropic.base-url:https://api.anthropic.com/v1/messages}")
    private String apiUrl;
    
    @Value("${spring.ai.anthropic.chat.options.model:claude-3-haiku-20240307}")
    private String model;
    
    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private static final String API_VERSION = "2023-06-01";
    private static final String SYSTEM_PROMPT = 
            "You are an AI assistant named Claude, developed by Anthropic. " +
            "You are helpful, harmless, and honest. " +
            "Always provide clear, concise, and accurate responses to the best of your ability.";
            
    // Store request metrics - using ConcurrentHashMap for thread safety
    private final ConcurrentHashMap<String, RequestMetrics> requestMetrics = new ConcurrentHashMap<>();

    public String generateResponse(String userPrompt) {
        String requestId = generateRequestId();
        Instant startTime = Instant.now();
        
        try {
            log.info("Generating response for prompt: {} (Request ID: {})", userPrompt, requestId);
            
            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("x-api-key", apiKey);
            headers.set("anthropic-version", API_VERSION);
            
            // Create request body
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", model);  // Use the model from properties
            requestBody.put("max_tokens", 1000);
            requestBody.put("temperature", 0.7);
            requestBody.put("system", SYSTEM_PROMPT);
            
            List<Map<String, Object>> messages = new ArrayList<>();
            Map<String, Object> message = new HashMap<>();
            message.put("role", "user");
            
            List<Map<String, String>> content = new ArrayList<>();
            Map<String, String> contentItem = new HashMap<>();
            contentItem.put("type", "text");
            contentItem.put("text", userPrompt);
            content.add(contentItem);
            
            message.put("content", content);
            messages.add(message);
            
            requestBody.put("messages", messages);
            
            // Send request
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            
            // Debug log of request headers and body
            log.info("API Key: {}", apiKey.substring(0, Math.min(5, apiKey.length())) + "..." + (apiKey.length() > 10 ? apiKey.substring(apiKey.length() - 5) : ""));
            log.info("Using model: {}", model);
            log.info("Request body: {}", requestBody);
            
            // Record time before API call
            Instant apiCallStart = Instant.now();
            
            // Parse response
            ClaudeResponse response = restTemplate.postForObject(apiUrl, entity, ClaudeResponse.class);
            
            // Record time after API call
            Instant apiCallEnd = Instant.now();
            Duration apiCallDuration = Duration.between(apiCallStart, apiCallEnd);
            
            if (response != null && response.content != null && !response.content.isEmpty()) {
                log.info("Response generated successfully");
                
                // Calculate full processing time
                Duration totalDuration = Duration.between(startTime, Instant.now());
                
                // Store metrics
                RequestMetrics metrics = new RequestMetrics();
                metrics.setRequestId(requestId);
                metrics.setPrompt(userPrompt);
                metrics.setModel(model);
                metrics.setRequestTimestamp(startTime);
                metrics.setResponseTimestamp(Instant.now());
                metrics.setApiCallDuration(apiCallDuration);
                metrics.setTotalProcessingDuration(totalDuration);
                metrics.setResponseLength(response.content.get(0).text.length());
                
                // Store in our metrics map
                requestMetrics.put(requestId, metrics);
                
                // Log the timing information
                log.info("Request metrics - ID: {}, API call duration: {}ms, Total processing time: {}ms, Response length: {} characters",
                        requestId, apiCallDuration.toMillis(), totalDuration.toMillis(), metrics.getResponseLength());
                
                return response.content.get(0).text;
            } else {
                log.error("Invalid response format from Claude API");
                recordFailedRequest(requestId, startTime, apiCallDuration, "Invalid response format");
                return "Sorry, I couldn't generate a response at this time.";
            }
        } catch (Exception e) {
            Duration totalDuration = Duration.between(startTime, Instant.now());
            recordFailedRequest(requestId, startTime, totalDuration, e.getMessage());
            log.error("Error calling Claude API", e);
            return "Error: " + e.getMessage();
        }
    }
    
    /**
     * Records metrics for a failed request
     */
    private void recordFailedRequest(String requestId, Instant startTime, Duration duration, String errorMessage) {
        RequestMetrics metrics = new RequestMetrics();
        metrics.setRequestId(requestId);
        metrics.setRequestTimestamp(startTime);
        metrics.setResponseTimestamp(Instant.now());
        metrics.setTotalProcessingDuration(duration);
        metrics.setSuccess(false);
        metrics.setErrorMessage(errorMessage);
        
        requestMetrics.put(requestId, metrics);
        log.info("Failed request metrics - ID: {}, Total processing time: {}ms, Error: {}", 
                requestId, duration.toMillis(), errorMessage);
    }
    
    /**
     * Generates a unique request ID
     */
    private String generateRequestId() {
        return "req_" + System.currentTimeMillis() + "_" + (int)(Math.random() * 1000);
    }
    
    /**
     * Get all stored request metrics
     */
    public Map<String, RequestMetrics> getAllRequestMetrics() {
        return new HashMap<>(requestMetrics);
    }
    
    /**
     * Get metrics for a specific request
     */
    public RequestMetrics getRequestMetrics(String requestId) {
        return requestMetrics.get(requestId);
    }
    
    /**
     * Clear all stored metrics
     */
    public void clearMetrics() {
        requestMetrics.clear();
    }
    
    /**
     * Class to store metrics for each request
     */
    @Data
    public static class RequestMetrics {
        private String requestId;
        private String prompt;
        private String model;
        private Instant requestTimestamp;
        private Instant responseTimestamp;
        private Duration apiCallDuration;
        private Duration totalProcessingDuration;
        private int responseLength;
        private boolean success = true;
        private String errorMessage;
    }
    
    // Response classes
    static class ClaudeResponse {
        @JsonProperty("content")
        private List<ContentItem> content;
        
        public List<ContentItem> getContent() {
            return content;
        }
        
        public void setContent(List<ContentItem> content) {
            this.content = content;
        }
    }
    
    static class ContentItem {
        @JsonProperty("type")
        private String type;
        
        @JsonProperty("text")
        private String text;
        
        public String getType() {
            return type;
        }
        
        public void setType(String type) {
            this.type = type;
        }
        
        public String getText() {
            return text;
        }
        
        public void setText(String text) {
            this.text = text;
        }
    }
} 