package com.example.Intellibus.models.report;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Request model for daily monitoring reports
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DailyMonitoringReportRequest extends ReportRequest {
    
    /**
     * Metrics to track for the day
     */
    private List<String> metricsToTrack;
    
    /**
     * Current metric values
     */
    private Map<String, Object> currentMetrics;
    
    /**
     * Previous period metric values for comparison
     */
    private Map<String, Object> previousPeriodMetrics;
    
    /**
     * Target or goal values for metrics
     */
    private Map<String, Object> targetMetrics;
    
    /**
     * Notable events or activities happening today
     */
    private List<String> notableEvents;
    
    /**
     * Time intervals for metric breakdowns (hourly, morning/afternoon, etc.)
     */
    private String timeInterval;
    
    /**
     * Whether to highlight trends
     */
    private boolean highlightTrends;
    
    /**
     * Whether to include alerts for metrics outside expected ranges
     */
    private boolean includeAlerts;
    
    /**
     * Alert thresholds for metrics
     */
    private Map<String, Object> alertThresholds;
} 