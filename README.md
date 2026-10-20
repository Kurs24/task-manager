# JavaTask - Task Management API

A RESTful API for task management built with Spring Boot and PostgreSQL.

## Project Specification

### Tech Stack
- **Java Version**: 21
- **Spring Boot**: 4.1.1
- **Build Tool**: Maven
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA (Hibernate)
- **Additional Libraries**:
  - Lombok (for reducing boilerplate code)
  - Spring Boot Validation (for request validation)

### Features
- Create, Read, Update, Delete (CRUD) operations for tasks
- Filter tasks by status (TODO, IN_PROGRESS, DONE)
- Automatic timestamp management (createdAt, updatedAt)
- RESTful API design with standardized response format
- Request validation with Jakarta Validation

### Project Structure
```
src/main/java/com/refresh/javatask/
├── JavataskApplication.java          # Main application entry point
├── controller/Rest/
│   └── TaskController.java           # REST API endpoints
├── entity/
│   ├── Task.java                     # Task entity with JPA annotations
│   └── APIResponse.java              # Standardized API response wrapper
├── repository/
│   └── TaskRepository.java           # JPA repository interface
├── requestDTO/
│   └── CreateTask.java               # DTO for task creation/update
└── service/
    ├── TaskService.java              # Service interface
    └── TaskServiceImpl.java          # Service implementation
```

## Prerequisites

Before running this project, ensure you have the following installed:

- **Java Development Kit (JDK) 21** or higher
- **Maven 3.6+** (for building the project)
- **PostgreSQL** (database server)

## Installation

### 1. Clone the Repository
```bash
git clone "this git repository link"
cd javatask
```

### 2. Configure Database

Create a PostgreSQL database for the application:
```sql
CREATE DATABASE javatask;
```

### 3. Set Environment Variables

Create a `.env` file in the project root directory with the following variables:

```env
DB_DATABASE=javatask
DB_USER=your_postgresql_username
DB_PASSWORD=your_postgresql_password
```

Or set them as system environment variables:
- Windows (PowerShell):
  ```powershell
  $env:DB_DATABASE="javatask"
  $env:DB_USER="your_username"
  $env:DB_PASSWORD="your_password"
  ```

- Windows (CMD):
  ```cmd
  set DB_DATABASE=javatask
  set DB_USER=your_username
  set DB_PASSWORD=your_password
  ```

### 4. Update Database Configuration

The database configuration is in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/${DB_DATABASE}
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWORD}
```

Ensure PostgreSQL is running on `localhost:5432` or update the URL accordingly.

## Running the Project

### Using Maven

1. **Build the project**:
   ```bash
   mvn clean install
   ```

2. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

### Using Java JAR

1. **Build the JAR file**:
   ```bash
   mvn clean package
   ```

2. **Run the JAR**:
   ```bash
   java -jar target/javatask-0.0.1-SNAPSHOT.jar
   ```

The application will start on `http://localhost:8080` by default.

## API Endpoints

Base URL: `http://localhost:8080/api/tasks`

### Create Task
- **Method**: `POST`
- **Endpoint**: `/api/tasks`
- **Request Body**:
  ```json
  {
    "title": "Complete project documentation",
    "status": "TODO",
    "dueDate": "2024-12-31T23:59:59"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Task successfully created",
    "data": {
      "id": 1,
      "title": "Complete project documentation",
      "status": "TODO",
      "dueDate": "2024-12-31T23:59:59",
      "createdAt": "2024-10-20T10:00:00",
      "updatedAt": "2024-10-20T10:00:00"
    }
  }
  ```

### Get Tasks by Status
- **Method**: `GET`
- **Endpoint**: `/api/tasks?status=TODO`
- **Query Parameters**: `status` (TODO, IN_PROGRESS, or DONE)
- **Response**:
  ```json
  {
    "message": "Task successfully retrieved",
    "data": [
      {
        "id": 1,
        "title": "Complete project documentation",
        "status": "TODO",
        "dueDate": "2024-12-31T23:59:59",
        "createdAt": "2024-10-20T10:00:00",
        "updatedAt": "2024-10-20T10:00:00"
      }
    ]
  }
  ```

### Get Task by ID
- **Method**: `GET`
- **Endpoint**: `/api/tasks/{id}`
- **Response**:
  ```json
  {
    "message": "Task successfully retrieved",
    "data": {
      "id": 1,
      "title": "Complete project documentation",
      "status": "TODO",
      "dueDate": "2024-12-31T23:59:59",
      "createdAt": "2024-10-20T10:00:00",
      "updatedAt": "2024-10-20T10:00:00"
    }
  }
  ```

### Update Task
- **Method**: `PUT`
- **Endpoint**: `/api/tasks/{id}`
- **Request Body**:
  ```json
  {
    "title": "Complete project documentation - Updated",
    "status": "IN_PROGRESS",
    "dueDate": "2024-12-31T23:59:59"
  }
  ```
- **Response**:
  ```json
  {
    "message": "Task successfully updated",
    "data": {
      "id": 1,
      "title": "Complete project documentation - Updated",
      "status": "IN_PROGRESS",
      "dueDate": "2024-12-31T23:59:59",
      "createdAt": "2024-10-20T10:00:00",
      "updatedAt": "2024-10-20T11:00:00"
    }
  }
  ```

### Delete Task
- **Method**: `DELETE`
- **Endpoint**: `/api/tasks/{id}`
- **Response**:
  ```json
  {
    "message": "Task successfully deleted",
    "data": 1
  }
  ```

## Task Status Values

- `TODO` - Task not started
- `IN_PROGRESS` - Task currently being worked on
- `DONE` - Task completed

## Database Schema

The application uses JPA with `ddl-auto=create-drop`, which means:
- Tables are automatically created on application startup
- Tables are dropped when the application shuts down

**Task Table Structure**:
- `id` (Long, Primary Key, Auto-generated)
- `title` (String)
- `status` (Enum: TODO, IN_PROGRESS, DONE)
- `dueDate` (LocalDateTime)
- `createdAt` (LocalDateTime)
- `updatedAt` (LocalDateTime)

## Testing

Run tests using Maven:
```bash
mvn test
```

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Verify database credentials in `.env` file
- Check that the database name matches in both PostgreSQL and `.env`

### Port Already in Use
If port 8080 is already in use, you can change it in `application.properties`:
```properties
server.port=8081
```

### Build Failures
- Ensure Java 21 is installed and configured: `java -version`
- Clean and rebuild: `mvn clean install`

## License

This project is licensed under the MIT License.
