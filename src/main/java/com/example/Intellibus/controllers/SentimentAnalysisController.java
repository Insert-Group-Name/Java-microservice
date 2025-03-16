package com.example.Intellibus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Intellibus.models.HealthResponse;
import com.example.Intellibus.models.sentiment.SentimentAnalysisRequest;
import com.example.Intellibus.models.sentiment.SentimentAnalysisResult;
import com.example.Intellibus.services.SentimentAnalysisService;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * Controller for sentiment analysis endpoints
 */
@RestController
@RequestMapping("/api/sentiment")
@Slf4j
public class SentimentAnalysisController {

    @Autowired
    private SentimentAnalysisService sentimentService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> healthCheck() {
        log.info("Health check request received for SentimentAnalysisController");
        return ResponseEntity.ok(new HealthResponse("SentimentAnalysisController"));
    }

    /**
     * Analyze sentiment of a single text
     */
    @PostMapping("/analyze")
    public SentimentAnalysisResult analyzeSentiment(@RequestBody SentimentAnalysisRequest request) {
        log.info("Received sentiment analysis request for text: {}", 
                request.getText().substring(0, Math.min(50, request.getText().length())) + "...");
        return sentimentService.analyzeSentiment(request);
    }
    
    /**
     * Asynchronously analyze sentiment of a single text
     */
    @PostMapping("/analyze/async")
    public CompletableFuture<SentimentAnalysisResult> analyzeSentimentAsync(@RequestBody SentimentAnalysisRequest request) {
        return CompletableFuture.supplyAsync(() -> analyzeSentiment(request));
    }
    
    /**
     * Batch analyze sentiment for multiple texts
     */
    @PostMapping("/analyze/batch")
    public SentimentAnalysisResult[] batchAnalyzeSentiment(@RequestBody SentimentAnalysisRequest[] requests) {
        log.info("Received batch sentiment analysis request for {} texts", requests.length);
        return sentimentService.batchAnalyzeSentiment(requests);
    }
} 