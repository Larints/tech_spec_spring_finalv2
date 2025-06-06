## User Subscription Management Microservice

This microservice provides a REST API for managing users and their subscriptions to various services. The project is built with Spring Boot 3 and includes essential functionality for creating, updating, retrieving, and deleting users and their subscriptions.

## Technologies Used

- **Java 17** for the application development

- **Spring Boot 3** for the main framework

- **JPA** for database management with PostgreSQL

- **Liquibase** for database migrations

- **MapStruct** for DTO mappings

- **Swagger** for API documentation

- **Docker** for containerization


## Swagger Documentation

The Swagger UI for testing and exploring the API is available at:

[Swagger UI](http://localhost:8080/swagger-ui/index.html)

## API Testing

API tests are located in the **"scratch"** folder, with the following filenames for testing:

- `AnalyticsApi.java`

- `SubscriptionApi.java`
- `UserApi.java`


## Running the Application

1. **Build the application** using Docker:

   bash

   `docker-compose up --build`

2. **Access the application** at `http://localhost:8080`.