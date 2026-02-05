# Travel Planner Application

![Tests](https://github.com/YOUR_USERNAME/YOUR_REPO_NAME/actions/workflows/maven-tests.yml/badge.svg)

REST API per la pianificazione di viaggi con gestione di città, tappe, ricerche e profili utente.

## Tecnologie

- **Java 17**
- **Spring Boot 3.5.10**
- **Spring Data JPA**
- **Spring Security** con autenticazione JWT
- **PostgreSQL** (produzione)
- **H2** (test)
- **Maven**
- **JUnit 5** per i test

## Funzionalità

- 🔐 Autenticazione JWT con registrazione e login
- 🌍 Gestione città con dati meteo e punti di interesse
- 🗺️ Creazione e gestione viaggi personalizzati
- 📍 Organizzazione di tappe (trip stops) con date e note
- 📊 Cronologia ricerche filtrata per utente
- 👤 Gestione profili utente
- 🏷️ Sistema di tag per categorizzare le città

## Esecuzione

### Pre-requisiti

- JDK 17 o superiore
- Maven 3.6+
- PostgreSQL 12+ (per produzione)

### Avvio applicazione

```bash
mvn spring-boot:run
```

L'applicazione sarà disponibile su `http://localhost:8080`

### Esecuzione test

```bash
mvn test
```

### Test Coverage

Il progetto include:
- Test di repository (`@DataJpaTest`)
- Test di service con Mockito
- Test di controller con MockMvc

## API Documentation

Una volta avviata l'applicazione, la documentazione Swagger è disponibile su:
- **Swagger UI**: `http://localhost:8080/swagger-ui.html`
- **OpenAPI JSON**: `http://localhost:8080/v3/api-docs`

## Configurazione

Configura il database in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/travel_planner
spring.datasource.username=your_username
spring.datasource.password=your_password
```

## CI/CD

Il progetto utilizza GitHub Actions per eseguire automaticamente i test ad ogni push:
- Build Maven automatico
- Esecuzione test suite completa
- Report dei risultati
- Upload degli artefatti di test

## Endpoint principali

- `POST /api/auth/register` - Registrazione utente
- `POST /api/auth/login` - Login e generazione token JWT
- `GET /api/cities` - Lista città
- `GET /api/search-history` - Cronologia ricerche dell'utente autenticato
- `GET /api/trips` - Viaggi dell'utente autenticato
- `GET /api/user/profile` - Profilo utente

## Autori

Progetto finale Academy

## Licenza

Progetto educativo
