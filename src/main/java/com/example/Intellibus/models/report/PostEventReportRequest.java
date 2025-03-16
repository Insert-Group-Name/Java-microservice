package com.example.Intellibus.models.report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Request model for post-event reports
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PostEventReportRequest extends ReportRequest {
    
    /**
     * Type of event (conference, hackathon, workshop, etc.)
     */
    private String eventType;
    
    /**
     * Location of the event
     */
    private String location;
    
    /**
     * Event date and time
     */
    private LocalDateTime eventDateTime;
    
    /**
     * Number of participants
     */
    private int participantCount;
    
    /**
     * List of sponsors or partners
     */
    private List<String> sponsors;
    
    /**
     * List of activities or sessions
     */
    private List<String> activities;
    
    /**
     * Participant feedback (can be raw text or structured data)
     */
    private List<String> participantFeedback;
    
    /**
     * Key performance indicators for the event
     */
    private Map<String, Object> kpis;
    
    /**
     * Whether to include recommendations for future events
     */
    private boolean includeRecommendations;
} 