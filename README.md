# Travel Planner Application

![Tests](https://github.com/acanicchio99/TravelPlanner/actions/workflows/maven-tests.yml/badge.svg)

REST API for travel planning with management of cities, trip stops, searches, and user profiles.

## Technologies

- **Java 17**
- **Spring Boot 3.5.10**
- **Spring Data JPA**
- **Spring Security** with JWT authentication
- **PostgreSQL** (production)
- **H2** (testing)
- **Maven**
- **JUnit 5** for testing

## Features

- 🔐 JWT authentication with user registration and login
- 🌍 City management with weather data and points of interest
- 🗺️ Creation and management of personalized trips
- 📍 Organization of trip stops with dates and notes
- 📊 Search history filtered by user
- 👤 User profile management
- 🏷️ Tag system for categorizing cities

## Running the Application

### Prerequisites

- JDK 17 or higher
- Maven 3.6+
- PostgreSQL 12+ (for production)

### Start the application

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`

### Run tests

```bash
mvn test
```

### Test Coverage

The project includes:

- Repository tests (`@DataJpaTest`)
- Service tests with Mockito
- Controller tests with MockMvc

## API Documentation

Once the application is running, the Swagger documentation is available at:

- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## Configuration

Configure the database in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/travel_planner
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## CI/CD

The project uses GitHub Actions to automatically run tests on every push:

- Automatic Maven build
- Execution of the complete test suite
- Test results reporting
- Test artifacts upload

## Main Endpoints

- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - Login and JWT token generation
- `GET /api/cities` - List of cities
- `GET /api/search-history` - Search history of the authenticated user
- `GET /api/trips` - Trips of the authenticated user
- `GET /api/user/profile` - User profile

## Authors

Final Academy Project

## License

Educational project
