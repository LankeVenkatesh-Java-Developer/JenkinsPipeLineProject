# Real-Time Product CRUD Application

A Spring Boot application with real-time CRUD operations using WebSocket, complete with authentication and authorization.

## Features

- **Real-time Updates**: WebSocket integration for live data synchronization
- **Authentication**: Secure login system with role-based access control
- **Authorization**: ADMIN and USER roles with different permissions
- **CRUD Operations**: Full Create, Read, Update, Delete functionality for products
- **Modern UI**: Bootstrap-based responsive interface
- **Docker Support**: Containerized deployment with Docker Compose

## Technology Stack

- **Backend**: Spring Boot 4.1.0, Java 21
- **Database**: MySQL 8.0
- **ORM**: Hibernate/JPA
- **Security**: Spring Security with BCrypt password encryption
- **Real-time**: WebSocket with STOMP messaging
- **Frontend**: Thymeleaf, Bootstrap 5, SockJS, Stomp.js
- **Build Tool**: Maven
- **Containerization**: Docker, Docker Compose

## Default Credentials

The application automatically creates default users on first startup:

### Admin User
- **Username**: `admin`
- **Password**: `admin123`
- **Role**: ADMIN (Full access - can create, update, delete products)

### Regular User
- **Username**: `user`
- **Password**: `user123`
- **Role**: USER (Read-only access - can only view products)

## Role Permissions

| Feature | ADMIN | USER |
|---------|-------|------|
| View Products | ✅ | ✅ |
| Create Products | ✅ | ❌ |
| Update Products | ✅ | ❌ |
| Delete Products | ✅ | ❌ |

## Quick Start

### Prerequisites
- Java 21 or higher
- Maven 3.6+
- MySQL 8.0 (or use Docker Compose)

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd DockerProjectIntegeration
   ```

2. **Configure Database**
   Update `src/main/resources/application.yaml` with your MySQL credentials:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/crud_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
       username: root
       password: your_password
   ```

3. **Build and Run**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Access the Application**
   - Open browser: `http://localhost:8080`
   - Login with default credentials

### Docker Deployment

See [DOCKER_README.md](DOCKER_README.md) for detailed Docker deployment instructions.

Quick start with Docker Compose:
```bash
docker-compose up -d
```

## Project Structure

```
src/main/java/com/ashok/it/dockerprojectintegeration/
├── Config/
│   ├── CustomUserDetailsService.java    # Spring Security user details
│   ├── DataInitializer.java              # Default user creation
│   ├── SecurityConfig.java               # Security configuration
│   └── WebSocketConfig.java              # WebSocket setup
├── Controller/
│   ├── AuthController.java               # Login/Registration endpoints
│   ├── ProductController.java            # Product CRUD API
│   ├── ViewController.java               # Page routing
│   └── WebSocketController.java          # WebSocket messaging
├── Model/
│   ├── Product.java                      # Product entity
│   └── User.java                         # User entity
├── Repository/
│   ├── ProductRepository.java            # Product data access
│   └── UserRepository.java               # User data access
├── Service/
│   ├── ProductService.java               # Product business logic
│   └── UserService.java                  # User business logic
└── DockerProjectIntegerationApplication.java
```

## API Endpoints

### Authentication
- `GET /login` - Login page
- `GET /register` - Registration page
- `POST /register` - Register new user
- `POST /logout` - Logout

### Products (REST API)
- `GET /api/products` - Get all products (ADMIN/USER)
- `GET /api/products/{id}` - Get product by ID (ADMIN/USER)
- `POST /api/products` - Create new product (ADMIN only)
- `PUT /api/products/{id}` - Update product (ADMIN only)
- `DELETE /api/products/{id}` - Delete product (ADMIN only)

### WebSocket
- `WS /ws` - WebSocket endpoint for real-time updates
- Topic: `/topic/products` - Product change notifications

## Configuration

### Environment Variables

The application supports the following environment variables for externalized configuration:

```env
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/crud_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=your_password

# JPA Configuration
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SPRING_JPA_SHOW_SQL=true
```

## Security

- Passwords are encrypted using BCrypt
- Session-based authentication
- CSRF protection disabled for API endpoints
- CORS enabled for cross-origin requests
- Role-based access control using Spring Security annotations

## Development

### Adding New Users

1. Register through the UI at `/register`
2. Or use the DataInitializer to add default users
3. Admin users can be created by setting role to ADMIN in registration

### Customizing Roles

Modify the `User.Role` enum in `User.java` to add new roles:
```java
public enum Role {
    ADMIN, USER, MANAGER, VIEWER
}
```

Update security annotations in controllers accordingly.

## Troubleshooting

### Database Connection Issues
- Ensure MySQL is running
- Check credentials in `application.yaml`
- Verify `allowPublicKeyRetrieval=true` is in JDBC URL

### WebSocket Not Connecting
- Check browser console for errors
- Verify WebSocket configuration in `WebSocketConfig.java`
- Ensure firewall allows WebSocket connections

### Authentication Issues
- Clear browser cookies
- Verify user exists in database
- Check password encryption in `UserService.java`

## License

This project is licensed under the MIT License.

## Support

For issues and questions, please refer to the project documentation or contact the development team.
# JenkinsPipeLineProject
