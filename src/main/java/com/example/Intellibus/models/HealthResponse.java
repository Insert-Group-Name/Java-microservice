package com.example.Intellibus.models;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Common health check response model
 */
@Data
public class HealthResponse {
    
    /**
     * Service status
     */
    private String status;
    
    /**
     * Controller name
     */
    private String controller;
    
    /**
     * Timestamp of the health check
     */
    private LocalDateTime timestamp;
    
    /**
     * Additional information
     */
    private String message;
    
    /**
     * Constructor for creating a health response
     */
    public HealthResponse(String controller) {
        this.status = "UP";
        this.controller = controller;
        this.timestamp = LocalDateTime.now();
        this.message = controller + " is operational";
    }
    
    /**
     * Constructor with additional message
     */
    public HealthResponse(String controller, String message) {
        this.status = "UP";
        this.controller = controller;
        this.timestamp = LocalDateTime.now();
        this.message = message;
    }
} 