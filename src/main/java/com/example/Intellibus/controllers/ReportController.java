package com.example.Intellibus.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Intellibus.models.HealthResponse;
import com.example.Intellibus.models.report.DailyMonitoringReportRequest;
import com.example.Intellibus.models.report.EngagementMetricsRequest;
import com.example.Intellibus.models.report.PostEventReportRequest;
import com.example.Intellibus.models.report.ReportRequest;
import com.example.Intellibus.models.report.ReportResponse;
import com.example.Intellibus.services.ReportGenerationService;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;

/**
 * Controller for report generation endpoints
 */
@RestController
@RequestMapping("/api/reports")
@Slf4j
public class ReportController {

    @Autowired
    private ReportGenerationService reportService;

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> healthCheck() {
        log.info("Health check request received for ReportController");
        return ResponseEntity.ok(new HealthResponse("ReportController"));
    }

    /**
     * Generic report generation endpoint
     */
    @PostMapping
    public ReportResponse generateReport(@RequestBody ReportRequest request) {
        log.info("Received request to generate a {} report: {}", request.getReportType(), request.getTitle());
        return reportService.generateReport(request);
    }
    
    /**
     * Engagement metrics report endpoint
     */
    @PostMapping("/engagement-metrics")
    public ReportResponse generateEngagementMetricsReport(@RequestBody EngagementMetricsRequest request) {
        log.info("Received request to generate engagement metrics report: {}", request.getTitle());
        return reportService.generateEngagementMetricsReport(request);
    }
    
    /**
     * Post-event report endpoint
     */
    @PostMapping("/post-event")
    public ReportResponse generatePostEventReport(@RequestBody PostEventReportRequest request) {
        log.info("Received request to generate post-event report: {}", request.getTitle());
        return reportService.generatePostEventReport(request);
    }
    
    /**
     * Daily monitoring report endpoint
     */
    @PostMapping("/daily-monitoring")
    public ReportResponse generateDailyMonitoringReport(@RequestBody DailyMonitoringReportRequest request) {
        log.info("Received request to generate daily monitoring report: {}", request.getTitle());
        return reportService.generateDailyMonitoringReport(request);
    }
    
    /**
     * Asynchronous generic report generation endpoint
     */
    @PostMapping("/async")
    public CompletableFuture<ReportResponse> generateReportAsync(@RequestBody ReportRequest request) {
        return CompletableFuture.supplyAsync(() -> generateReport(request));
    }
    
    /**
     * Asynchronous engagement metrics report endpoint
     */
    @PostMapping("/engagement-metrics/async")
    public CompletableFuture<ReportResponse> generateEngagementMetricsReportAsync(@RequestBody EngagementMetricsRequest request) {
        return CompletableFuture.supplyAsync(() -> generateEngagementMetricsReport(request));
    }
    
    /**
     * Asynchronous post-event report endpoint
     */
    @PostMapping("/post-event/async")
    public CompletableFuture<ReportResponse> generatePostEventReportAsync(@RequestBody PostEventReportRequest request) {
        return CompletableFuture.supplyAsync(() -> generatePostEventReport(request));
    }
    
    /**
     * Asynchronous daily monitoring report endpoint
     */
    @PostMapping("/daily-monitoring/async")
    public CompletableFuture<ReportResponse> generateDailyMonitoringReportAsync(@RequestBody DailyMonitoringReportRequest request) {
        return CompletableFuture.supplyAsync(() -> generateDailyMonitoringReport(request));
    }
} 