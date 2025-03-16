package com.example.Intellibus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Intellibus.services.ClaudeAIService;
import com.example.Intellibus.services.ClaudeAIService.RequestMetrics;

import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    @Autowired
    private ClaudeAIService claudeAIService;
    
    /**
     * Get all request metrics
     */
    @GetMapping
    public ResponseEntity<Map<String, RequestMetrics>> getAllMetrics() {
        return ResponseEntity.ok(claudeAIService.getAllRequestMetrics());
    }
    
    /**
     * Get metrics for a specific request
     */
    @GetMapping("/{requestId}")
    public ResponseEntity<RequestMetrics> getMetricsById(@PathVariable String requestId) {
        RequestMetrics metrics = claudeAIService.getRequestMetrics(requestId);
        if (metrics != null) {
            return ResponseEntity.ok(metrics);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * Clear all metrics
     */
    @PostMapping("/clear")
    public ResponseEntity<Void> clearMetrics() {
        claudeAIService.clearMetrics();
        return ResponseEntity.ok().build();
    }
} 