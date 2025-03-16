# Intellibus - AI-Powered Sentiment Analysis and Chat Insights

This Spring Boot microservice uses Claude AI to provide sentiment analysis for text, insights from chat conversations, and generates various types of reports.

## Features

### 1. Sentiment Analysis
Analyze the sentiment of text to determine if it's positive, negative, neutral, or mixed:

- Overall sentiment classification
- Sentiment score from -1.0 to 1.0
- Confidence level
- Dominant emotions detected
- Key phrases that influenced the sentiment
- Detailed insights

### 2. Chat Conversation Analysis
Get deep insights from chat conversations:

- Overall sentiment
- Main topics discussed
- User intents identified
- Key questions asked
- Issues or problems detected
- Action items that could be followed up on
- Message-by-message sentiment and analysis

### 3. Report Generation
Generate comprehensive reports with AI-driven insights:

- **Engagement Metrics Reports**: Analyze user engagement metrics
- **Post-Event Reports**: Summarize event outcomes and feedback
- **Daily Monitoring Reports**: Track key metrics throughout the day

## API Endpoints

### Sentiment Analysis

#### Analyze a single text
```
POST /api/sentiment/analyze
```
Request body:
```json
{
  "text": "Your text to analyze",
  "context": "Optional context",
  "source": "Optional source information"
}
```

#### Batch analyze multiple texts
```
POST /api/sentiment/analyze/batch
```
Request body:
```json
[
  {
    "text": "First text to analyze"
  },
  {
    "text": "Second text to analyze"
  }
]
```

### Chat Analysis

#### Analyze a conversation
```
POST /api/chat-analysis
```
Request body:
```json
{
  "messages": [
    {
      "role": "user",
      "content": "Hello, I'm having trouble with my order #12345",
      "timestamp": "2023-05-01T14:32:00Z"
    },
    {
      "role": "agent",
      "content": "I'm sorry to hear that. What seems to be the issue with your order?",
      "timestamp": "2023-05-01T14:33:00Z"
    },
    {
      "role": "user",
      "content": "I ordered it 3 weeks ago and it still hasn't arrived.",
      "timestamp": "2023-05-01T14:34:00Z"
    }
  ],
  "context": "customer_support",
  "userId": "user_123"
}
```

### Report Generation

#### Generate Engagement Metrics Report
```
POST /api/reports/engagement-metrics
```
Request body:
```json
{
  "title": "Q2 2023 User Engagement Report",
  "startDate": "2023-04-01",
  "endDate": "2023-06-30",
  "metricsToInclude": ["active_users", "session_duration", "retention_rate"],
  "breakdownCategories": {
    "demographics": ["age", "location", "device_type"],
    "behavior": ["new_users", "returning_users"]
  },
  "audience": "Mobile App Users",
  "channels": ["mobile_app", "web", "email"],
  "includeSentimentAnalysis": true,
  "context": "Analyzing user engagement after recent app update"
}
```

#### Generate Post-Event Report
```
POST /api/reports/post-event
```
Request body:
```json
{
  "title": "Tech Conference 2023 Report",
  "eventType": "conference",
  "location": "New York Tech Center",
  "eventDateTime": "2023-09-15T09:00:00Z",
  "participantCount": 250,
  "sponsors": ["TechCorp", "DevInc", "CloudSystems"],
  "activities": [
    "Keynote: Future of AI",
    "Workshop: Cloud Deployment Strategies",
    "Panel Discussion: Cybersecurity Trends"
  ],
  "participantFeedback": [
    "Great keynote speaker, very insightful!",
    "The workshops were hands-on and practical.",
    "Would have liked more networking opportunities."
  ],
  "kpis": {
    "attendance_rate": 92,
    "satisfaction_score": 4.2,
    "speaker_ratings": 4.5
  },
  "includeRecommendations": true,
  "context": "First in-person event after pandemic"
}
```

#### Generate Daily Monitoring Report
```
POST /api/reports/daily-monitoring
```
Request body:
```json
{
  "title": "System Performance - Daily Report",
  "startDate": "2023-10-15",
  "metricsToTrack": ["response_time", "error_rate", "user_signups", "active_sessions"],
  "currentMetrics": {
    "response_time": 235,
    "error_rate": 0.5,
    "user_signups": 127,
    "active_sessions": 4568
  },
  "previousPeriodMetrics": {
    "response_time": 250,
    "error_rate": 0.7,
    "user_signups": 115,
    "active_sessions": 4320
  },
  "targetMetrics": {
    "response_time": 200,
    "error_rate": 0.3,
    "user_signups": 150,
    "active_sessions": 5000
  },
  "notableEvents": [
    "Deployed new search feature at 10:00 AM",
    "Marketing campaign launched at 2:30 PM"
  ],
  "timeInterval": "hourly",
  "highlightTrends": true,
  "includeAlerts": true,
  "alertThresholds": {
    "response_time": 300,
    "error_rate": 1.0
  }
}
```

#### Asynchronous Report Generation
All report endpoints also support asynchronous processing by appending `/async` to the endpoint:
```
POST /api/reports/engagement-metrics/async
POST /api/reports/post-event/async
POST /api/reports/daily-monitoring/async
```

### Health Check Endpoints

#### Main Health Check
```
GET /health
```
Response:
```json
{
  "status": "UP",
  "timestamp": "2023-11-21T14:30:45.123",
  "services": [
    {"service": "ClaudeAIService", "status": "UP"},
    {"service": "SentimentAnalysisService", "status": "UP"},
    {"service": "ChatAnalysisService", "status": "UP"},
    {"service": "ReportGenerationService", "status": "UP"}
  ],
  "message": "All services are operational"
}
```

#### Detailed Health Check
```
GET /health/details
```
Response:
```json
{
  "status": "UP",
  "timestamp": "2023-11-21T14:30:45.123",
  "components": {
    "claudeAI": {
      "status": "UP",
      "details": {
        "requestCount": 152,
        "service": "Claude AI"
      }
    },
    "sentimentAnalysis": {
      "status": "UP",
      "details": {
        "service": "Sentiment Analysis"
      }
    },
    "chatAnalysis": {
      "status": "UP",
      "details": {
        "service": "Chat Analysis"
      }
    },
    "reportGeneration": {
      "status": "UP",
      "details": {
        "service": "Report Generation"
      }
    }
  },
  "message": "All services are operational"
}
```

#### Individual Service Health Checks
Each service also has its own health check endpoint:
```
GET /api/chat/health
GET /api/sentiment/health
GET /api/chat-analysis/health
GET /api/reports/health
```
Response:
```json
{
  "status": "UP",
  "controller": "SentimentAnalysisController",
  "timestamp": "2023-11-21T14:30:45.123",
  "message": "SentimentAnalysisController is operational"
}
```

## Configuration

The application uses Claude AI via the Anthropic API. Configuration is managed through environment variables:

1. Set your Anthropic API key as an environment variable:
   ```
   ANTHROPIC_API_KEY=your_api_key_here
   ```

2. Configure the application using `application.properties` or environment variables:
   - `spring.ai.anthropic.chat.options.model` - The Claude model to use
   - `spring.ai.anthropic.chat.options.temperature` - Temperature setting (0.0-1.0)
   - `spring.ai.anthropic.chat.options.max-tokens` - Maximum tokens in response

## Getting Started

1. Clone this repository
2. Set the `ANTHROPIC_API_KEY` environment variable with your Claude API key
3. Run the application with Maven:
   ```
   ./mvnw spring-boot:run
   ```

## Example Usage

### Sentiment Analysis Example

Analyze customer feedback:

```
curl -X POST \
  http://localhost:8080/api/sentiment/analyze \
  -H 'Content-Type: application/json' \
  -d '{
    "text": "I absolutely love your product! It has made my life so much easier and I use it every day. The customer service was also fantastic when I had a minor issue.",
    "source": "customer_feedback"
  }'
```

### Chat Analysis Example

Analyze a customer service conversation:

```
curl -X POST \
  http://localhost:8080/api/chat-analysis \
  -H 'Content-Type: application/json' \
  -d '{
    "messages": [
      {"role": "user", "content": "I need to cancel my subscription."},
      {"role": "agent", "content": "I understand you want to cancel. May I ask why you're leaving?"},
      {"role": "user", "content": "The service is too expensive for what it offers."},
      {"role": "agent", "content": "I understand your concern about pricing. We have a more affordable plan that might work better for your needs."},
      {"role": "user", "content": "What would that include?"}
    ],
    "context": "subscription_cancellation"
  }'
``` 