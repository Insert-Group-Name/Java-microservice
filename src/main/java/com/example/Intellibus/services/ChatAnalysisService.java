package com.example.Intellibus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Intellibus.models.chat.ChatAnalysisRequest;
import com.example.Intellibus.models.chat.ChatAnalysisRequest.ChatMessage;
import com.example.Intellibus.models.chat.ChatAnalysisResult;
import com.example.Intellibus.models.chat.ChatAnalysisResult.MessageAnalytics;
import com.example.Intellibus.models.sentiment.SentimentAnalysisRequest;
import com.example.Intellibus.models.sentiment.SentimentAnalysisResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ChatAnalysisService {

    @Autowired
    private ClaudeAIService claudeAIService;
    
    @Autowired
    private SentimentAnalysisService sentimentService;
    
    @Autowired
    private ObjectMapper objectMapper;
    

    /**
     * Analyzes a chat conversation to provide insights
     * 
     * @param request The chat analysis request containing conversation messages
     * @return A detailed chat analysis result
     */
    public ChatAnalysisResult analyzeChat(ChatAnalysisRequest request) {
        Instant startTime = Instant.now();
        String requestId = UUID.randomUUID().toString();
        
        try {
            log.info("Analyzing chat conversation with {} messages", request.getMessages().size());
            
            // Build a format suitable for prompting
            String chatText = formatChatForPrompt(request);
            
            // Create a prompt for Claude that asks for structured chat analysis
            String prompt = "Analyze the following conversation. Respond with ONLY a JSON object containing chat analysis details.\n\n" +
                           "Conversation to analyze:\n" + chatText + "\n\n" +
                           "Remember to respond with ONLY a JSON object with these fields:\n" +
                           "- main_topics: array of main topics discussed\n" +
                           "- user_intents: array of identified user intentions\n" +
                           "- key_questions: array of important questions asked\n" +
                           "- identified_issues: array of issues or problems in the conversation\n" +
                           "- action_items: array of action items extracted from the conversation\n" +
                           "- conversation_summary: a concise summary of the conversation";
            
            // Call Claude API for overall analysis
            String responseText = claudeAIService.generateResponse(prompt);
            
            // Extract the JSON from the response
            String jsonStr = extractJsonFromResponse(responseText);
            
            // Parse the JSON into our structure
            JsonNode analysisNode = objectMapper.readTree(jsonStr);
            
            // Create the result
            ChatAnalysisResult result = new ChatAnalysisResult();
            
            // Get overall sentiment for the entire conversation
            String fullText = request.getMessages().stream()
                    .map(ChatMessage::getContent)
                    .collect(Collectors.joining(" "));
            
            SentimentAnalysisRequest sentimentRequest = new SentimentAnalysisRequest();
            sentimentRequest.setText(fullText);
            SentimentAnalysisResult overallSentiment = sentimentService.analyzeSentiment(sentimentRequest);
            
            // Set values from analysis
            result.setOverallSentiment(overallSentiment);
            
            // Parse the fields from the JSON response
            if (analysisNode.has("main_topics")) {
                result.setMainTopics(
                    objectMapper.convertValue(analysisNode.get("main_topics"), 
                    objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, String.class)));
            }
            
            if (analysisNode.has("user_intents")) {
                result.setUserIntents(
                    objectMapper.convertValue(analysisNode.get("user_intents"), 
                    objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, String.class)));
            }
            
            if (analysisNode.has("key_questions")) {
                result.setKeyQuestions(
                    objectMapper.convertValue(analysisNode.get("key_questions"), 
                    objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, String.class)));
            }
            
            if (analysisNode.has("identified_issues")) {
                result.setIdentifiedIssues(
                    objectMapper.convertValue(analysisNode.get("identified_issues"), 
                    objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, String.class)));
            }
            
            if (analysisNode.has("action_items")) {
                result.setActionItems(
                    objectMapper.convertValue(analysisNode.get("action_items"), 
                    objectMapper.getTypeFactory().constructCollectionType(java.util.List.class, String.class)));
            }
            
            if (analysisNode.has("conversation_summary")) {
                result.setConversationSummary(analysisNode.get("conversation_summary").asText());
            }
            
            // Analyze individual messages
            Map<Integer, MessageAnalytics> messageAnalytics = new HashMap<>();
            for (int i = 0; i < request.getMessages().size(); i++) {
                ChatMessage message = request.getMessages().get(i);
                
                // Only analyze substantive messages
                if (message.getContent() != null && message.getContent().trim().length() > 5) {
                    SentimentAnalysisRequest msgSentimentRequest = new SentimentAnalysisRequest();
                    msgSentimentRequest.setText(message.getContent());
                    SentimentAnalysisResult msgSentiment = sentimentService.analyzeSentiment(msgSentimentRequest);
                    
                    MessageAnalytics msgAnalytics = new MessageAnalytics();
                    msgAnalytics.setSentiment(msgSentiment);
                    
                    // For simplicity, we'll just check if message contains a question mark
                    msgAnalytics.setContainsQuestion(message.getContent().contains("?"));
                    
                    // Set some default values that would normally come from deeper analysis
                    msgAnalytics.setImportance(5); // Medium importance by default
                    
                    messageAnalytics.put(i, msgAnalytics);
                }
            }
            
            result.setMessageAnalytics(messageAnalytics);
            result.setRequestId(requestId);
            result.setProcessingTimeMs(Duration.between(startTime, Instant.now()).toMillis());
            
            return result;
            
        } catch (Exception e) {
            log.error("Error analyzing chat", e);
            
            // Create a minimal result with error information
            ChatAnalysisResult errorResult = new ChatAnalysisResult();
            errorResult.setRequestId(requestId);
            errorResult.setProcessingTimeMs(Duration.between(startTime, Instant.now()).toMillis());
            errorResult.setConversationSummary("Error analyzing chat: " + e.getMessage());
            
            return errorResult;
        }
    }
    
    /**
     * Format the chat for prompting Claude
     */
    private String formatChatForPrompt(ChatAnalysisRequest request) {
        StringBuilder sb = new StringBuilder();
        
        for (ChatMessage message : request.getMessages()) {
            sb.append(message.getRole().toUpperCase())
              .append(": ")
              .append(message.getContent())
              .append("\n\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Extract JSON object from Claude's response text
     */
    private String extractJsonFromResponse(String responseText) {
        int startIdx = responseText.indexOf('{');
        int endIdx = responseText.lastIndexOf('}') + 1;
        
        if (startIdx >= 0 && endIdx > startIdx) {
            return responseText.substring(startIdx, endIdx);
        }
        
        return responseText;
    }
} 