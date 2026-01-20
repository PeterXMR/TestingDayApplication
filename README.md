# Testing day application stub

* [Assignment](https://docs.google.com/document/d/1_N8WLocdokIcb6KhfmqLUcMkvevoOqmHFRRDBA3MUzw/edit#)

## Docker Setup

**`docker-compose.yml`** 
— Prepares just the database

**`docker-compose.prod.yml`** 
— Creates both the database and app configured to work together

## CI/CD Pipeline
GitHub Actions automatically:
- Builds the application and runs all tests with each commit pushed to the `main` branch
- Builds a multi-platform Docker image (linux/amd64, linux/arm64)
- Pushes the image to GitHub Container Registry (GHCR)

The Docker image is available at: 
`https://github.com/PeterXMR/TestingDayApplication/pkgs/container/testingdayapplication`

## API Documentation (Swagger UI)

The application includes auto-generated API documentation powered by SpringDoc OpenAPI.

**Access points:**
- **Interactive Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI Specification (JSON):** http://localhost:8080/v3/api-docs
- **OpenAPI Specification (YAML):** http://localhost:8080/v3/api-docs.yaml
