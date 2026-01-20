#!/bin/bash

echo "Starting validation of Exchange Rates API..."

# Wait for app to be accessible
MAX_ATTEMPTS=30
ATTEMPT=0
ENDPOINT="http://localhost:8080/api/v1/rates/2022-06-20"

echo "Testing endpoint: $ENDPOINT"

while [ $ATTEMPT -lt $MAX_ATTEMPTS ]; do
  RESPONSE=$(curl -s "$ENDPOINT" 2>/dev/null)

  # Check if response contains expected fields
  if echo "$RESPONSE" | grep -q '"base"' && echo "$RESPONSE" | grep -q '"rates"'; then
    echo "✓ API is responding with valid data"
    echo "Response sample: $(echo $RESPONSE | head -c 100)..."
    exit 0
  fi

  ATTEMPT=$((ATTEMPT + 1))
  if [ $ATTEMPT -lt $MAX_ATTEMPTS ]; then
    echo "Attempt $ATTEMPT/$MAX_ATTEMPTS: Waiting for API to respond with data..."
    sleep 2
  fi
done

echo "✗ API did not respond with valid data after $MAX_ATTEMPTS attempts"
echo "Final response: $RESPONSE"
exit 1
