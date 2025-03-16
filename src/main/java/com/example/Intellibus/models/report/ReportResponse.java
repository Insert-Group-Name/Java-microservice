package com.example.Intellibus.models.report;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response model for report generation
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse {
    
    /**
     * Unique identifier for the report
     */
    private String reportId;
    
    /**
     * Type of report generated
     */
    private String reportType;
    
    /**
     * Title of the report
     */
    private String title;
    
    /**
     * When the report was generated
     */
    private LocalDateTime generatedAt;
    
    /**
     * Executive summary of the report
     */
    @JsonProperty("executive_summary")
    private String executiveSummary;
    
    /**
     * Key findings or insights
     */
    @JsonProperty("key_findings")
    private List<String> keyFindings;
    
    /**
     * Detailed sections of the report
     */
    private List<ReportSection> sections;
    
    /**
     * Recommendations based on the findings
     */
    private List<String> recommendations;
    
    /**
     * Raw metrics data in the report
     */
    @JsonProperty("metrics_data")
    private Map<String, Object> metricsData;
    
    /**
     * Visual elements like charts (stored as descriptions or references)
     */
    @JsonProperty("visual_elements")
    private List<VisualElement> visualElements;
    
    /**
     * Time taken to generate the report (ms)
     */
    private long processingTimeMs;
    
    /**
     * Inner class representing a section in the report
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportSection {
        /**
         * Section title
         */
        private String title;
        
        /**
         * Section content
         */
        private String content;
        
        /**
         * Any subsections
         */
        private List<ReportSection> subsections;
    }
    
    /**
     * Inner class representing a visual element in the report
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VisualElement {
        /**
         * Type of visual (chart, graph, table)
         */
        private String type;
        
        /**
         * Title of the visual
         */
        private String title;
        
        /**
         * Description of what the visual represents
         */
        private String description;
        
        /**
         * Data for the visual (would be rendered by the client)
         */
        private Map<String, Object> data;
    }
} 