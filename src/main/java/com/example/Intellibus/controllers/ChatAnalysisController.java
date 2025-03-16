package com.example.Intellibus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Intellibus.models.chat.ChatAnalysisRequest;
import com.example.Intellibus.models.chat.ChatAnalysisResult;
import com.example.Intellibus.services.ChatAnalysisService;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * Controller for chat conversation analysis endpoints
 */
@RestController
@RequestMapping("/api/chat-analysis")
@Slf4j
public class ChatAnalysisController {

    @Autowired
    private ChatAnalysisService chatAnalysisService;

    /**
     * Analyze a chat conversation
     */
    @PostMapping
    public ChatAnalysisResult analyzeChat(@RequestBody ChatAnalysisRequest request) {
        log.info("Received chat analysis request with {} messages", request.getMessages().size());
        return chatAnalysisService.analyzeChat(request);
    }
    
    /**
     * Asynchronously analyze a chat conversation
     */
    @PostMapping("/async")
    public CompletableFuture<ChatAnalysisResult> analyzeChatAsync(@RequestBody ChatAnalysisRequest request) {
        return CompletableFuture.supplyAsync(() -> analyzeChat(request));
    }
} 