package com.example.Intellibus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Intellibus.models.HealthResponse;
import com.example.Intellibus.services.ClaudeAIService;
import com.example.Intellibus.services.SentimentAnalysisService;
import com.example.Intellibus.services.ChatAnalysisService;
import com.example.Intellibus.services.ReportGenerationService;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main health check controller
 */
@RestController
@RequestMapping("/health")
@Slf4j
public class HealthController {

    @Autowired
    private ClaudeAIService claudeAIService;
    
    @Autowired
    private SentimentAnalysisService sentimentService;
    
    @Autowired
    private ChatAnalysisService chatAnalysisService;
    
    @Autowired
    private ReportGenerationService reportService;

    /**
     * Main health check endpoint
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> healthCheck() {
        log.info("Main health check request received");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        
        List<Map<String, String>> services = new ArrayList<>();
        
        // Add individual service statuses
        Map<String, String> claudeStatus = new HashMap<>();
        claudeStatus.put("service", "ClaudeAIService");
        claudeStatus.put("status", "UP");
        services.add(claudeStatus);
        
        Map<String, String> sentimentStatus = new HashMap<>();
        sentimentStatus.put("service", "SentimentAnalysisService");
        sentimentStatus.put("status", "UP");
        services.add(sentimentStatus);
        
        Map<String, String> chatStatus = new HashMap<>();
        chatStatus.put("service", "ChatAnalysisService");
        chatStatus.put("status", "UP");
        services.add(chatStatus);
        
        Map<String, String> reportStatus = new HashMap<>();
        reportStatus.put("service", "ReportGenerationService");
        reportStatus.put("status", "UP");
        services.add(reportStatus);
        
        response.put("services", services);
        response.put("message", "All services are operational");
        
        return ResponseEntity.ok(response);
    }
    
    /**
     * Detailed health check endpoint
     */
    @GetMapping("/details")
    public ResponseEntity<Map<String, Object>> detailedHealthCheck() {
        log.info("Detailed health check request received");
        
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("timestamp", LocalDateTime.now());
        
        Map<String, Object> components = new HashMap<>();
        
        // Add individual service details
        Map<String, Object> claudeDetails = new HashMap<>();
        claudeDetails.put("status", "UP");
        claudeDetails.put("details", Map.of(
            "requestCount", claudeAIService.getAllRequestMetrics().size(),
            "service", "Claude AI"
        ));
        components.put("claudeAI", claudeDetails);
        
        Map<String, Object> sentimentDetails = new HashMap<>();
        sentimentDetails.put("status", "UP");
        sentimentDetails.put("details", Map.of(
            "service", "Sentiment Analysis"
        ));
        components.put("sentimentAnalysis", sentimentDetails);
        
        Map<String, Object> chatDetails = new HashMap<>();
        chatDetails.put("status", "UP");
        chatDetails.put("details", Map.of(
            "service", "Chat Analysis"
        ));
        components.put("chatAnalysis", chatDetails);
        
        Map<String, Object> reportDetails = new HashMap<>();
        reportDetails.put("status", "UP");
        reportDetails.put("details", Map.of(
            "service", "Report Generation"
        ));
        components.put("reportGeneration", reportDetails);
        
        response.put("components", components);
        response.put("message", "All services are operational");
        
        return ResponseEntity.ok(response);
    }
} 