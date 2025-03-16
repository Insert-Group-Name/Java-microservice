package com.example.Intellibus.models.report;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base request model for report generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    /**
     * Type of report to generate
     * (e.g., "engagement_metrics", "post_event", "daily_monitoring")
     */
    private String reportType;
    
    /**
     * Title or name of the event/project
     */
    private String title;
    
    /**
     * Date range for the report (start date)
     */
    private LocalDate startDate;
    
    /**
     * Date range for the report (end date)
     */
    private LocalDate endDate;
    
    /**
     * Raw data to analyze (can be metrics, feedback, etc.)
     */
    private List<Map<String, Object>> data;
    
    /**
     * Additional context or instructions for report generation
     */
    private String context;
    
    /**
     * Tags or categories for classification
     */
    private List<String> tags;
} 