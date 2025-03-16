# Intellibus - AI-Powered Sentiment Analysis and Chat Insights

This Spring Boot microservice uses Claude AI to provide sentiment analysis for text and insights from chat conversations.

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