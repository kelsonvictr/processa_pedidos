
# Processa Pedidos API

![image](https://github.com/kelsonvictr/processa_pedidos/assets/54041512/8928cc5b-5293-4e06-8ca0-04ad349c8509)

## Overview
The Processa Pedidos API is a Java-based application for efficient order processing. It integrates PostgreSQL for data management, RabbitMQ for message handling, and SMTP for email functionalities.

## Features
- Order processing management
- PostgreSQL database integration
- RabbitMQ for message queuing
- Email notifications

## Configuration
This API uses PostgreSQL, RabbitMQ, and SMTP configurations for its operations. It's important to have an installation of RabbitMQ according to the settings provided in the `application.properties` file.

## Installation Guide

### Prerequisites
- Java (Version 17 or later)
- PostgreSQL
- RabbitMQ
- Maven

### Installation Steps
1. Clone the repository:
2. Navigate to the project directory:
3. Configure `application.properties` with your database, RabbitMQ, and email settings.
4. **For the database installation, run the command `docker-compose up -d` to set up the PostgreSQL database using Docker.**
5. Build the project with Maven:
6. Run the application

### Additional Information
- **Ensure you have RabbitMQ installed and configured as per the `application.properties` file.**
- **To access the API documentation, visit: http://localhost:8080/swagger-ui/index.html after starting the application.**
