# SistemaScuolaLingue - School Management System

A Java-based school management system for managing language courses, students, teachers, and administrative staff.

## Features

- Student (Cliente) management: registration, course enrollment, payments
- Teacher (Docente) management: assignment to courses
- Administrative staff (Segreteria) management: payment processing, enrollment management, statistics
- Manager (Gestore) management: course organization, staff management
- Web interface with role-based access control
- Docker support for easy deployment
- Authentication system for secure access

## Architecture

The application follows a layered architecture:
- **Boundary Layer**: Console UI components (original interface)
- **Controller Layer**: Business logic orchestration (`GestioneScuolaController`)
- **Service Layer**: Service interfaces and implementations
- **Data Access Layer**: DAO classes for database operations
- **Entity Layer**: JPA/Hibernate annotated classes
- **Utilities**: Password security, email service, etc.
- **Web Layer**: Spring MVC controllers and Thymeleaf templates (new interface)

## Prerequisites

- Java Development Kit (JDK) 26 or later
- Maven 3.9+
- Docker and Docker Compose (for containerized deployment)
- PostgreSQL 17+ (if not using Docker)

## Running the Application

### Option 1: Run Directly with Maven

1. Ensure PostgreSQL is running with:
   - Database: `gagandeepsuman`
   - Username: `gagandeepsuman`
   - Password: `Gaggu123`

2. Build and run the application:
   ```bash
   mvn spring-boot:run
   ```

3. Access the web interface at: http://localhost:8080

### Option 2: Run with Docker Compose

1. Start the application and database:
   ```bash
   docker-compose up --build
   ```

2. Access the web interface at: http://localhost:8080

3. To stop the application:
   ```bash
   docker-compose down
   ```

### Option 3: Run the JAR Directly

1. Build the application:
   ```bash
   mvn clean package -DskipTests
   ```

2. Run the JAR:
   ```bash
   java -jar target/SistemaScuolaLingue-1.0-SNAPSHOT.jar
   ```

## Configuration

The application can be configured through:
- `application.properties` file
- Environment variables (especially useful for Docker)

Key configuration options:
- Database connection: `spring.datasource.url`, `spring.datasource.username`, `spring.datasource.password`
- Hibernate settings: `spring.jpa.hibernate.ddl-auto` (set to `update` for development, `validate` for production)
- Server port: `SERVER_PORT` environment variable or `server.port` property
- Active profiles: `SPRING_PROFILES_ACTIVE` environment variable

## Web Interface Access

The web interface provides role-based access:

- **Main Menu**: http://localhost:8080/
- **Login Page**: http://localhost:8080/login
- **Cliente Menu**: http://localhost:8080/cliente (after login)
- **Gestore Menu**: http://localhost:8080/gestore (after login)
- **Segreteria Menu**: http://localhost:8080/segreteria (after login)

### Publicly Accessible Endpoints
- Course catalog: http://localhost:8080/cliente/corsi (visible to all users)
- Login/logout endpoints

### Protected Endpoints (Require Authentication)
All other endpoints require appropriate authentication and role-based authorization.

## Default Credentials

For initial access, you can use the following default credentials (these can be overridden via environment variables):

### Gestore (Manager)
- Username: `gestore_admin`
- Password: `Gest0reP@ss`

### Segreteria (Administrative Staff)
- Username: `segreteria_admin`
- Password: `Segre3t@r1a`

### Cliente (Student)
Clienti credentials are auto-generated during registration and sent via email.

## Docker Deployment

The application is configured for easy deployment using Docker Compose.

### Docker Images
- `scuolalingue-app`: The Spring Boot application
- `postgres:17-alpine`: PostgreSQL database

### Volumes
- `postgres_data`: Persistent storage for PostgreSQL data

### Networking
The application and database communicate via an internal Docker network. The application connects to the database using the hostname `postgres` as defined in the docker-compose.yml file.

### Environment Variables
The following environment variables can be customized:
- `SPRING_DATASOURCE_URL`: Database JDBC URL
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password
- `GESTORE_USERNAME`: Gestore login username
- `GESTORE_PASSWORD`: Gestore login password
- `SEGRETERIA_USERNAME`: Segreteria login username
- `SEGRETERIA_PASSWORD`: Segreteria login password
- `SPRING_PROFILES_ACTIVE`: Active Spring profiles (default: prod)
- `SERVER_PORT`: Server port (default: 8080)

## Database Initialization

On first startup, the application will:
1. Create tables based on JPA entities (when `spring.jpa.hibernate.ddl-auto=update`)
2. Initialize sample data from `src/main/resources/data.sql` (if present)

The data.sql file contains:
- Table creation for `impiegati_segreteria` (segreteria employees) for gestore management functionality

## Development

### Backward Compatibility
The original console-based interface remains fully functional and unchanged. Users can still interact with the system via the terminal using the original Boundary classes.

### Running Tests
```bash
mvn test
```

### Building
```bash
mvn clean package
```

## Features Implemented

### Core Features
- Student registration and profile management
- Course catalog browsing and enrollment
- Payment processing and tracking
- Teacher and course management
- Administrative statistics and reporting
- Role-based access control
- Authentication system with session management
- Password security using PBKDF2 hashing
- Email notifications (configure via EmailService)

### Web Interface Features
- Responsive design using Bootstrap 5
- Role-based menu visibility
- Form validation and feedback
- Flash messages for success/error notifications
- Course catalog with public browsing
- Secure enrollment for authenticated students
- Staff management interfaces for gestore and segreteria roles

## Troubleshooting

### Common Issues

1. **Port already in use**: If port 8080 is occupied, either:
   - Stop the existing process using the port
   - Configure the application to use a different port:
     ```bash
     java -jar target/SistemaScuolaLingue-1.0-SNAPSHOT.jar --server.port=8081
     ```
     Or set the SERVER_PORT environment variable in Docker.

2. **Database connection failed**: Ensure PostgreSQL is running and the connection details in application.properties or environment variables are correct.

3. **Migration errors**: If you encounter Hibernate schema validation errors, temporarily set `spring.jpa.hibernate.ddl-auto=update` in application.properties.

4. **Mail service not working**: Configure EmailService with valid Gmail SMTP credentials for real email sending.

## License

This project is for educational purposes as part of a software engineering elaborato.

## Contact

For questions or support, please refer to the project documentation or contact the development team.