# Task Management System

A simple Task Management System built using Spring Boot, PostgreSQL, and JWT authentication. This application allows users to register, log in, create tasks, assign them, and manage comments. It provides role-based access for administrators and users.

## Features
- **User Registration**: Register users with email and password.
- **User Authentication**: Login with email and password to receive a JWT token for subsequent requests.
- **Task Management**: Create, update, delete, and retrieve tasks.
- **Role-based Access**: Administrators can manage all tasks, while regular users can only manage their own tasks.
- **Comment Management**: Users can add comments to tasks.

## Technologies
- Spring Boot
- PostgreSQL
- JWT Authentication
- JPA (Hibernate)
- Spring Security
- Swagger for API Documentation
- Docker for containerization

## Prerequisites

- **Java 17** or higher
- **PostgreSQL** database running locally or in Docker
- **Maven** for building the project

## Setup Instructions


### 1. Clone the repository
```bash
git clone https://github.com/your-repository/task-management-system.git
cd task-management-system


# Run the following command to start the database:

``` bash
docker-compose up --build


# To build the project, run:

``` bash
mvn clean install

# To start the Spring Boot application:

``` bash
mvn spring-boot:run