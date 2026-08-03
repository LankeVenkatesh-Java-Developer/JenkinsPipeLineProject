# Docker Deployment Guide

This project includes Docker support for containerized deployment of the Real-Time CRUD application.

## Prerequisites

- Docker installed on your system
- Docker Compose installed (usually comes with Docker Desktop)

## Quick Start with Docker Compose

### 1. Build and Start Services

```bash
docker-compose up -d
```

This will:
- Build the Spring Boot application Docker image
- Start MySQL 8.0 container
- Start the application container
- Create necessary networks and volumes

### 2. View Logs

```bash
# View all logs
docker-compose logs -f

# View specific service logs
docker-compose logs -f app
docker-compose logs -f mysql
```

### 3. Stop Services

```bash
docker-compose down
```

### 4. Stop and Remove Volumes (Complete Cleanup)

```bash
docker-compose down -v
```

## Manual Docker Build

### Build the Image

```bash
docker build -t crud-app:latest .
```

### Run with MySQL

```bash
# Start MySQL
docker run -d --name crud-mysql \
  -e MYSQL_ROOT_PASSWORD=Venky@314 \
  -e MYSQL_DATABASE=crud_db \
  -p 3306:3306 \
  mysql:8.0

# Wait for MySQL to start, then run the app
docker run -d --name crud-app \
  --link crud-mysql:mysql \
  -p 8080:8080 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/crud_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true \
  -e SPRING_DATASOURCE_USERNAME=root \
  -e SPRING_DATASOURCE_PASSWORD=Venky@314 \
  crud-app:latest
```

## Environment Variables

The application supports the following environment variables for externalized configuration:

### Database Configuration
- `SPRING_DATASOURCE_URL` - JDBC connection URL (default: localhost MySQL)
- `SPRING_DATASOURCE_USERNAME` - Database username (default: root)
- `SPRING_DATASOURCE_PASSWORD` - Database password (default: Venky@314)

### JPA Configuration
- `SPRING_JPA_HIBERNATE_DDL_AUTO` - Schema generation strategy (default: update)
- `SPRING_JPA_SHOW_SQL` - Enable SQL logging (default: true)

## Access the Application

Once the containers are running:

- **Application UI**: http://localhost:8080
- **REST API**: http://localhost:8080/api/products
- **MySQL**: localhost:3306

## Custom Configuration

### Using Custom .env File

1. Create a `.env` file in the project root
2. Add your custom environment variables
3. Run `docker-compose up -d`

Example `.env` file:
```env
MYSQL_ROOT_PASSWORD=your_password
MYSQL_DATABASE=crud_db
MYSQL_USER=crud_user
MYSQL_PASSWORD=crud_password
SPRING_DATASOURCE_USERNAME=crud_user
SPRING_DATASOURCE_PASSWORD=crud_password
```

### Using Custom Configuration File

Mount your custom `application.yaml`:

```bash
docker run -d --name crud-app \
  -v /path/to/custom/application.yaml:/app/config/application.yaml \
  -p 8080:8080 \
  crud-app:latest
```

## Health Checks

The Dockerfile includes a health check that monitors the application:

```bash
# Check container health
docker inspect --format='{{.State.Health.Status}}' crud-app

# View health check logs
docker inspect --format='{{json .State.Health}}' crud-app
```

## Troubleshooting

### Application won't start
- Check if MySQL is healthy: `docker-compose ps`
- View application logs: `docker-compose logs app`
- Ensure MySQL port 3306 is not already in use

### Database connection issues
- Verify environment variables in `.env` file
- Check MySQL container logs: `docker-compose logs mysql`
- Ensure `allowPublicKeyRetrieval=true` is in the JDBC URL

### Rebuild after code changes
```bash
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

## Production Deployment

For production deployment:

1. Change MySQL root password in `.env`
2. Use `SPRING_JPA_HIBERNATE_DDL_AUTO: validate` instead of `update`
3. Set `SPRING_JPA_SHOW_SQL: false`
4. Use proper volume mounts for data persistence
5. Consider using Docker secrets for sensitive data

Example production docker-compose override:
```yaml
services:
  mysql:
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
  app:
    environment:
      SPRING_JPA_HIBERNATE_DDL_AUTO: validate
      SPRING_JPA_SHOW_SQL: "false"
```
