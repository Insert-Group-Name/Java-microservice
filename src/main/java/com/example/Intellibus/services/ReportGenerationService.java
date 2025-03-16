package com.example.Intellibus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Intellibus.models.report.DailyMonitoringReportRequest;
import com.example.Intellibus.models.report.EngagementMetricsRequest;
import com.example.Intellibus.models.report.PostEventReportRequest;
import com.example.Intellibus.models.report.ReportRequest;
import com.example.Intellibus.models.report.ReportResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class ReportGenerationService {

    @Autowired
    private ClaudeAIService claudeAIService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    /**
     * Generates a report based on the request type
     * 
     * @param request The base report request
     * @return The generated report
     */
    public ReportResponse generateReport(ReportRequest request) {
        if (request instanceof EngagementMetricsRequest) {
            return generateEngagementMetricsReport((EngagementMetricsRequest) request);
        } else if (request instanceof PostEventReportRequest) {
            return generatePostEventReport((PostEventReportRequest) request);
        } else if (request instanceof DailyMonitoringReportRequest) {
            return generateDailyMonitoringReport((DailyMonitoringReportRequest) request);
        } else {
            return generateGenericReport(request);
        }
    }
    
    /**
     * Generates an engagement metrics report
     * 
     * @param request The engagement metrics request
     * @return The generated report
     */
    public ReportResponse generateEngagementMetricsReport(EngagementMetricsRequest request) {
        Instant startTime = Instant.now();
        String reportId = UUID.randomUUID().toString();
        
        try {
            log.info("Generating engagement metrics report: {}", request.getTitle());
            
            // Build prompt for Claude
            String prompt = buildEngagementMetricsPrompt(request);
            
            // Call Claude API
            String responseText = claudeAIService.generateResponse(prompt);
            
            // Log the raw response for debugging
            log.debug("Raw Claude response: {}", responseText);
            
            // Extract the JSON from the response
            String jsonStr = extractJsonFromResponse(responseText);
            
            // Parse the JSON response
            ReportResponse report = parseReportResponse(jsonStr, reportId, "engagement_metrics", request.getTitle());
            report.setProcessingTimeMs(Duration.between(startTime, Instant.now()).toMillis());
            
            return report;
            
        } catch (Exception e) {
            log.error("Error generating engagement metrics report: {}", e.getMessage(), e);
            return createErrorReport(reportId, "engagement_metrics", request.getTitle(), e.getMessage(), startTime);
        }
    }
    
    /**
     * Generates a post-event report
     * 
     * @param request The post-event report request
     * @return The generated report
     */
    public ReportResponse generatePostEventReport(PostEventReportRequest request) {
        Instant startTime = Instant.now();
        String reportId = UUID.randomUUID().toString();
        
        try {
            log.info("Generating post-event report: {}", request.getTitle());
            
            // Build prompt for Claude
            String prompt = buildPostEventPrompt(request);
            
            // Call Claude API
            String responseText = claudeAIService.generateResponse(prompt);
            
            // Log the raw response for debugging
            log.debug("Raw Claude response: {}", responseText);
            
            // Extract the JSON from the response
            String jsonStr = extractJsonFromResponse(responseText);
            
            // Parse the JSON response
            ReportResponse report = parseReportResponse(jsonStr, reportId, "post_event", request.getTitle());
            report.setProcessingTimeMs(Duration.between(startTime, Instant.now()).toMillis());
            
            return report;
            
        } catch (Exception e) {
            log.error("Error generating post-event report: {}", e.getMessage(), e);
            return createErrorReport(reportId, "post_event", request.getTitle(), e.getMessage(), startTime);
        }
    }
    
    /**
     * Generates a daily monitoring report
     * 
     * @param request The daily monitoring report request
     * @return The generated report
     */
    public ReportResponse generateDailyMonitoringReport(DailyMonitoringReportRequest request) {
        Instant startTime = Instant.now();
        String reportId = UUID.randomUUID().toString();
        
        try {
            log.info("Generating daily monitoring report: {}", request.getTitle());
            
            // Build prompt for Claude
            String prompt = buildDailyMonitoringPrompt(request);
            
            // Call Claude API
            String responseText = claudeAIService.generateResponse(prompt);
            
            // Log the raw response for debugging
            log.debug("Raw Claude response: {}", responseText);
            
            // Extract the JSON from the response
            String jsonStr = extractJsonFromResponse(responseText);
            
            // Parse the JSON response
            ReportResponse report = parseReportResponse(jsonStr, reportId, "daily_monitoring", request.getTitle());
            report.setProcessingTimeMs(Duration.between(startTime, Instant.now()).toMillis());
            
            return report;
            
        } catch (Exception e) {
            log.error("Error generating daily monitoring report: {}", e.getMessage(), e);
            return createErrorReport(reportId, "daily_monitoring", request.getTitle(), e.getMessage(), startTime);
        }
    }
    
    /**
     * Generates a generic report
     * 
     * @param request The generic report request
     * @return The generated report
     */
    public ReportResponse generateGenericReport(ReportRequest request) {
        Instant startTime = Instant.now();
        String reportId = UUID.randomUUID().toString();
        
        try {
            log.info("Generating generic report: {}", request.getTitle());
            
            // Build prompt for Claude
            String prompt = buildGenericReportPrompt(request);
            
            // Call Claude API
            String responseText = claudeAIService.generateResponse(prompt);
            
            // Log the raw response for debugging
            log.debug("Raw Claude response: {}", responseText);
            
            // Extract the JSON from the response
            String jsonStr = extractJsonFromResponse(responseText);
            
            // Parse the JSON response
            ReportResponse report = parseReportResponse(jsonStr, reportId, request.getReportType(), request.getTitle());
            report.setProcessingTimeMs(Duration.between(startTime, Instant.now()).toMillis());
            
            return report;
            
        } catch (Exception e) {
            log.error("Error generating generic report: {}", e.getMessage(), e);
            return createErrorReport(reportId, request.getReportType(), request.getTitle(), e.getMessage(), startTime);
        }
    }
    
    /**
     * Builds a prompt for engagement metrics reports
     */
    private String buildEngagementMetricsPrompt(EngagementMetricsRequest request) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Generate a detailed engagement metrics report with the following specifications. ");
        prompt.append("Respond with ONLY a JSON object structured according to the format specified at the end.\n\n");
        
        // Add report details
        prompt.append("Report Title: ").append(request.getTitle()).append("\n");
        prompt.append("Time Period: ").append(request.getStartDate()).append(" to ").append(request.getEndDate()).append("\n");
        
        if (request.getAudience() != null && !request.getAudience().isEmpty()) {
            prompt.append("Target Audience: ").append(request.getAudience()).append("\n");
        }
        
        // Add metrics to include
        if (request.getMetricsToInclude() != null && !request.getMetricsToInclude().isEmpty()) {
            prompt.append("\nMetrics to include:\n");
            for (String metric : request.getMetricsToInclude()) {
                prompt.append("- ").append(metric).append("\n");
            }
        }
        
        // Add breakdown categories
        if (request.getBreakdownCategories() != null && !request.getBreakdownCategories().isEmpty()) {
            prompt.append("\nBreakdown categories:\n");
            for (Map.Entry<String, List<String>> entry : request.getBreakdownCategories().entrySet()) {
                prompt.append("- ").append(entry.getKey()).append(": ").append(String.join(", ", entry.getValue())).append("\n");
            }
        }
        
        // Add channels
        if (request.getChannels() != null && !request.getChannels().isEmpty()) {
            prompt.append("\nEngagement channels: ").append(String.join(", ", request.getChannels())).append("\n");
        }
        
        // Add sentiment analysis requirement
        if (request.isIncludeSentimentAnalysis()) {
            prompt.append("\nPlease include sentiment analysis of participant feedback.\n");
        }
        
        // Add raw data if available
        if (request.getData() != null && !request.getData().isEmpty()) {
            prompt.append("\nRaw data for analysis:\n");
            prompt.append(objectToString(request.getData())).append("\n");
        }
        
        // Add additional context
        if (request.getContext() != null && !request.getContext().isEmpty()) {
            prompt.append("\nAdditional context: ").append(request.getContext()).append("\n");
        }
        
        // Add instructions for the response format
        prompt.append("\nRespond with ONLY a JSON object with these fields:\n");
        prompt.append("- executive_summary: A concise overview of the engagement metrics\n");
        prompt.append("- key_findings: Array of the most important insights from the data\n");
        prompt.append("- sections: Array of report sections, each with title, content, and optional subsections\n");
        prompt.append("- recommendations: Array of actionable recommendations based on the findings\n");
        prompt.append("- metrics_data: Processed metrics data with calculated values\n");
        prompt.append("- visual_elements: Descriptions of charts/graphs that should be included\n");
        
        return prompt.toString();
    }
    
    /**
     * Builds a prompt for post-event reports
     */
    private String buildPostEventPrompt(PostEventReportRequest request) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Generate a comprehensive post-event report with the following specifications. ");
        prompt.append("Respond with ONLY a JSON object structured according to the format specified at the end.\n\n");
        
        // Add event details
        prompt.append("Event Title: ").append(request.getTitle()).append("\n");
        prompt.append("Event Type: ").append(request.getEventType()).append("\n");
        prompt.append("Date: ").append(request.getEventDateTime()).append("\n");
        prompt.append("Location: ").append(request.getLocation()).append("\n");
        prompt.append("Participants: ").append(request.getParticipantCount()).append("\n");
        
        // Add sponsors
        if (request.getSponsors() != null && !request.getSponsors().isEmpty()) {
            prompt.append("\nSponsors: ").append(String.join(", ", request.getSponsors())).append("\n");
        }
        
        // Add activities
        if (request.getActivities() != null && !request.getActivities().isEmpty()) {
            prompt.append("\nActivities/Sessions:\n");
            for (String activity : request.getActivities()) {
                prompt.append("- ").append(activity).append("\n");
            }
        }
        
        // Add KPIs
        if (request.getKpis() != null && !request.getKpis().isEmpty()) {
            prompt.append("\nKey Performance Indicators:\n");
            prompt.append(objectToString(request.getKpis())).append("\n");
        }
        
        // Add participant feedback
        if (request.getParticipantFeedback() != null && !request.getParticipantFeedback().isEmpty()) {
            prompt.append("\nParticipant Feedback:\n");
            for (String feedback : request.getParticipantFeedback()) {
                prompt.append("- ").append(feedback).append("\n");
            }
        }
        
        // Add recommendations requirement
        if (request.isIncludeRecommendations()) {
            prompt.append("\nPlease include recommendations for future events.\n");
        }
        
        // Add raw data if available
        if (request.getData() != null && !request.getData().isEmpty()) {
            prompt.append("\nAdditional data for analysis:\n");
            prompt.append(objectToString(request.getData())).append("\n");
        }
        
        // Add additional context
        if (request.getContext() != null && !request.getContext().isEmpty()) {
            prompt.append("\nAdditional context: ").append(request.getContext()).append("\n");
        }
        
        // Add instructions for the response format
        prompt.append("\nRespond with ONLY a JSON object with these fields:\n");
        prompt.append("- executive_summary: A concise overview of the event's success and outcomes\n");
        prompt.append("- key_findings: Array of the most important insights from the event\n");
        prompt.append("- sections: Array of report sections, each with title, content, and optional subsections\n");
        prompt.append("- recommendations: Array of suggestions for future events\n");
        prompt.append("- metrics_data: Key metrics and their values\n");
        prompt.append("- visual_elements: Descriptions of charts/graphs that should be included\n");
        
        return prompt.toString();
    }
    
    /**
     * Builds a prompt for daily monitoring reports
     */
    private String buildDailyMonitoringPrompt(DailyMonitoringReportRequest request) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Generate a daily monitoring report with the following specifications. ");
        prompt.append("Respond with ONLY a JSON object structured according to the format specified at the end.\n\n");
        
        // Add report details
        prompt.append("Report Title: ").append(request.getTitle()).append("\n");
        prompt.append("Date: ").append(request.getStartDate()).append("\n");
        prompt.append("Time Interval: ").append(request.getTimeInterval()).append("\n");
        
        // Add metrics to track
        if (request.getMetricsToTrack() != null && !request.getMetricsToTrack().isEmpty()) {
            prompt.append("\nMetrics to track:\n");
            for (String metric : request.getMetricsToTrack()) {
                prompt.append("- ").append(metric).append("\n");
            }
        }
        
        // Add current metrics
        if (request.getCurrentMetrics() != null && !request.getCurrentMetrics().isEmpty()) {
            prompt.append("\nCurrent metrics:\n");
            prompt.append(objectToString(request.getCurrentMetrics())).append("\n");
        }
        
        // Add previous period metrics
        if (request.getPreviousPeriodMetrics() != null && !request.getPreviousPeriodMetrics().isEmpty()) {
            prompt.append("\nPrevious period metrics (for comparison):\n");
            prompt.append(objectToString(request.getPreviousPeriodMetrics())).append("\n");
        }
        
        // Add target metrics
        if (request.getTargetMetrics() != null && !request.getTargetMetrics().isEmpty()) {
            prompt.append("\nTarget metrics:\n");
            prompt.append(objectToString(request.getTargetMetrics())).append("\n");
        }
        
        // Add notable events
        if (request.getNotableEvents() != null && !request.getNotableEvents().isEmpty()) {
            prompt.append("\nNotable events:\n");
            for (String event : request.getNotableEvents()) {
                prompt.append("- ").append(event).append("\n");
            }
        }
        
        // Add trend and alert requirements
        if (request.isHighlightTrends()) {
            prompt.append("\nPlease highlight significant trends in the data.\n");
        }
        
        if (request.isIncludeAlerts()) {
            prompt.append("\nPlease include alerts for metrics outside expected ranges.\n");
            
            if (request.getAlertThresholds() != null && !request.getAlertThresholds().isEmpty()) {
                prompt.append("Alert thresholds:\n");
                prompt.append(objectToString(request.getAlertThresholds())).append("\n");
            }
        }
        
        // Add raw data if available
        if (request.getData() != null && !request.getData().isEmpty()) {
            prompt.append("\nAdditional data for analysis:\n");
            prompt.append(objectToString(request.getData())).append("\n");
        }
        
        // Add additional context
        if (request.getContext() != null && !request.getContext().isEmpty()) {
            prompt.append("\nAdditional context: ").append(request.getContext()).append("\n");
        }
        
        // Add instructions for the response format
        prompt.append("\nRespond with ONLY a JSON object with these fields:\n");
        prompt.append("- executive_summary: A concise overview of the day's performance\n");
        prompt.append("- key_findings: Array of the most important insights from the data\n");
        prompt.append("- sections: Array of report sections, each with title, content, and optional subsections\n");
        prompt.append("- recommendations: Array of actionable recommendations\n");
        prompt.append("- metrics_data: Key metrics and their values, with comparisons to targets and previous periods\n");
        prompt.append("- visual_elements: Descriptions of charts/graphs that should be included\n");
        
        return prompt.toString();
    }
    
    /**
     * Builds a prompt for generic reports
     */
    private String buildGenericReportPrompt(ReportRequest request) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Generate a ").append(request.getReportType()).append(" report with the following specifications. ");
        prompt.append("Respond with ONLY a JSON object structured according to the format specified at the end.\n\n");
        
        // Add report details
        prompt.append("Report Title: ").append(request.getTitle()).append("\n");
        
        if (request.getStartDate() != null && request.getEndDate() != null) {
            prompt.append("Time Period: ").append(request.getStartDate()).append(" to ").append(request.getEndDate()).append("\n");
        }
        
        // Add tags if available
        if (request.getTags() != null && !request.getTags().isEmpty()) {
            prompt.append("\nTags: ").append(String.join(", ", request.getTags())).append("\n");
        }
        
        // Add raw data if available
        if (request.getData() != null && !request.getData().isEmpty()) {
            prompt.append("\nData for analysis:\n");
            prompt.append(objectToString(request.getData())).append("\n");
        }
        
        // Add additional context
        if (request.getContext() != null && !request.getContext().isEmpty()) {
            prompt.append("\nAdditional context: ").append(request.getContext()).append("\n");
        }
        
        // Add instructions for the response format
        prompt.append("\nRespond with ONLY a JSON object with these fields:\n");
        prompt.append("- executive_summary: A concise overview of the report findings\n");
        prompt.append("- key_findings: Array of the most important insights\n");
        prompt.append("- sections: Array of report sections, each with title, content, and optional subsections\n");
        prompt.append("- recommendations: Array of actionable recommendations\n");
        prompt.append("- metrics_data: Any relevant metrics and their values\n");
        prompt.append("- visual_elements: Descriptions of charts/graphs that should be included\n");
        
        return prompt.toString();
    }
    
    /**
     * Parse JSON response into ReportResponse object
     */
    private ReportResponse parseReportResponse(String jsonStr, String reportId, String reportType, String title) throws Exception {
        JsonNode rootNode = objectMapper.readTree(jsonStr);
        
        ReportResponse response = new ReportResponse();
        response.setReportId(reportId);
        response.setReportType(reportType);
        response.setTitle(title);
        response.setGeneratedAt(LocalDateTime.now());
        
        // Parse executive summary
        if (rootNode.has("executive_summary")) {
            response.setExecutiveSummary(rootNode.get("executive_summary").asText());
        }
        
        // Parse key findings
        if (rootNode.has("key_findings")) {
            List<String> findings = new ArrayList<>();
            rootNode.get("key_findings").forEach(node -> findings.add(node.asText()));
            response.setKeyFindings(findings);
        }
        
        // Parse recommendations
        if (rootNode.has("recommendations")) {
            List<String> recommendations = new ArrayList<>();
            rootNode.get("recommendations").forEach(node -> recommendations.add(node.asText()));
            response.setRecommendations(recommendations);
        }
        
        // Parse metrics data
        if (rootNode.has("metrics_data")) {
            response.setMetricsData(objectMapper.convertValue(rootNode.get("metrics_data"), Map.class));
        }
        
        // Parse sections
        if (rootNode.has("sections")) {
            List<ReportResponse.ReportSection> sections = new ArrayList<>();
            rootNode.get("sections").forEach(sectionNode -> {
                try {
                    ReportResponse.ReportSection section = new ReportResponse.ReportSection();
                    section.setTitle(sectionNode.has("title") ? sectionNode.get("title").asText() : "");
                    section.setContent(sectionNode.has("content") ? sectionNode.get("content").asText() : "");
                    
                    // Parse subsections if any
                    if (sectionNode.has("subsections")) {
                        List<ReportResponse.ReportSection> subsections = new ArrayList<>();
                        sectionNode.get("subsections").forEach(subNode -> {
                            ReportResponse.ReportSection subsection = new ReportResponse.ReportSection();
                            subsection.setTitle(subNode.has("title") ? subNode.get("title").asText() : "");
                            subsection.setContent(subNode.has("content") ? subNode.get("content").asText() : "");
                            subsections.add(subsection);
                        });
                        section.setSubsections(subsections);
                    }
                    
                    sections.add(section);
                } catch (Exception e) {
                    log.warn("Error parsing section: {}", e.getMessage());
                }
            });
            response.setSections(sections);
        }
        
        // Parse visual elements
        if (rootNode.has("visual_elements")) {
            List<ReportResponse.VisualElement> visualElements = new ArrayList<>();
            rootNode.get("visual_elements").forEach(visualNode -> {
                try {
                    ReportResponse.VisualElement visual = new ReportResponse.VisualElement();
                    visual.setType(visualNode.has("type") ? visualNode.get("type").asText() : "chart");
                    visual.setTitle(visualNode.has("title") ? visualNode.get("title").asText() : "");
                    visual.setDescription(visualNode.has("description") ? visualNode.get("description").asText() : "");
                    
                    if (visualNode.has("data")) {
                        visual.setData(objectMapper.convertValue(visualNode.get("data"), Map.class));
                    }
                    
                    visualElements.add(visual);
                } catch (Exception e) {
                    log.warn("Error parsing visual element: {}", e.getMessage());
                }
            });
            response.setVisualElements(visualElements);
        }
        
        return response;
    }
    
    /**
     * Create an error report
     */
    private ReportResponse createErrorReport(String reportId, String reportType, String title, String errorMessage, Instant startTime) {
        ReportResponse errorReport = new ReportResponse();
        errorReport.setReportId(reportId);
        errorReport.setReportType(reportType);
        errorReport.setTitle(title);
        errorReport.setGeneratedAt(LocalDateTime.now());
        errorReport.setExecutiveSummary("Error generating report: " + errorMessage);
        
        List<String> findings = new ArrayList<>();
        findings.add("An error occurred during report generation.");
        errorReport.setKeyFindings(findings);
        
        List<ReportResponse.ReportSection> sections = new ArrayList<>();
        ReportResponse.ReportSection errorSection = new ReportResponse.ReportSection();
        errorSection.setTitle("Error Details");
        errorSection.setContent("The report generation process encountered an error: " + errorMessage);
        sections.add(errorSection);
        errorReport.setSections(sections);
        
        errorReport.setProcessingTimeMs(Duration.between(startTime, Instant.now()).toMillis());
        
        return errorReport;
    }
    
    /**
     * Convert an object to a string representation
     */
    private String objectToString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            return obj.toString();
        }
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
