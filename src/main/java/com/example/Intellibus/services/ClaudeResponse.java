package com.example.Intellibus.services;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

// Response classes
class ClaudeResponse {
    @JsonProperty("content") List<ContentItem> content;
    
    public List<ContentItem> getContent() {
        return content;
    }
    
    public void setContent(List<ContentItem> content) {
        this.content = content;
    }
}