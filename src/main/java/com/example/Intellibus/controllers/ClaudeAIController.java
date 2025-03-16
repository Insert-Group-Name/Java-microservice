package com.example.Intellibus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Intellibus.models.ChatRequest;
import com.example.Intellibus.models.ChatResponse;
import com.example.Intellibus.models.HealthResponse;
import com.example.Intellibus.services.ClaudeAIService;
import com.example.Intellibus.services.ClaudeAIService.RequestMetrics;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/chat")
@Slf4j
public class ClaudeAIController {

    @Autowired
    private ClaudeAIService claudeAIService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> healthCheck() {
        log.info("Health check request received for ClaudeAIController");
        return ResponseEntity.ok(new HealthResponse("ClaudeAIController"));
    }

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        log.info("Received chat request with prompt: {}", request.getPrompt());
        
        String responseText = claudeAIService.generateResponse(request.getPrompt());
        
        // Get the latest metrics for this prompt (assuming the most recent one is for this request)
        RequestMetrics metrics = claudeAIService.getAllRequestMetrics().values().stream()
                .filter(m -> request.getPrompt().equals(m.getPrompt()))
                .findFirst()
                .orElse(null);
        
        ChatResponse response = new ChatResponse(responseText, "claude-3");
        
        // Add timing information if available
        if (metrics != null) {
            response.setRequestId(metrics.getRequestId());
            response.setProcessingTimeMs(metrics.getTotalProcessingDuration().toMillis());
            
            if (metrics.getApiCallDuration() != null) {
                response.setApiCallTimeMs(metrics.getApiCallDuration().toMillis());
            }
        }
        
        return response;
    }
    
    /**
     * Async version of the chat request
     */
    @PostMapping("/async")
    public CompletableFuture<ChatResponse> chatAsync(@RequestBody ChatRequest request) {
        return CompletableFuture.supplyAsync(() -> chat(request));
    }

    @GetMapping("test")
    public String test() {
        return "Hello World";
    }
} 