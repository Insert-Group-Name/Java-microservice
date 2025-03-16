package com.example.Intellibus.models.report;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Request model for engagement metrics reports
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class EngagementMetricsRequest extends ReportRequest {
    
    /**
     * Metrics to include in the report
     * (e.g., "participation_rate", "satisfaction_score", "comments_count")
     */
    private List<String> metricsToInclude;
    
    /**
     * Metrics grouped by categories (e.g., demographics, roles, etc.)
     */
    private Map<String, List<String>> breakdownCategories;
    
    /**
     * Target audience or participant group
     */
    private String audience;
    
    /**
     * Engagement channels (e.g., "web", "mobile", "in-person")
     */
    private List<String> channels;
    
    /**
     * Include sentiment analysis from participant feedback
     */
    private boolean includeSentimentAnalysis;
} 