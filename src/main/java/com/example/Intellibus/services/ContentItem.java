package com.example.Intellibus.services;

import com.fasterxml.jackson.annotation.JsonProperty;

class ContentItem {
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("text") String text;
    
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