# User Management System

A complete Spring Boot application with REST APIs and a modern web interface built with Thymeleaf and Bootstrap 5.

## Quick Start

### Prerequisites
- Java 24
- MySQL Server (localhost:3306)
- Maven (or use included Maven wrapper)

### Run the Application

1. **Setup Database:**
   ```bash
   mvnw.cmd flyway:clean flyway:migrate
   ```

2. **Start Application:**
   ```bash
   mvnw.cmd spring-boot:run
   ```

3. **Access the Application:**
   ```
   http://localhost:8080
   ```

## Features

### Web UI
- ✅ View all users with sorting
- ✅ Create new user
- ✅ Edit user information
- ✅ View user details
- ✅ Delete user
- ✅ Responsive design (mobile-friendly)
- ✅ Flash messages for actions

### REST API
- ✅ GET `/users` - List all users
- ✅ GET `/users/{id}` - Get user by ID
- ✅ POST `/users` - Create new user
- ✅ PUT `/users/{id}` - Update user
- ✅ DELETE `/users/{id}` - Delete user
- ✅ POST `/users/{id}/change-password` - Change password

## Sample Data

The application comes with 5 sample users:
1. John Doe (john.doe@example.com)
2. Jane Smith (jane.smith@example.com)
3. Alice Johnson (alice.johnson@example.com)
4. Bob Williams (bob.williams@example.com)
5. Charlie Brown (charlie.brown@example.com)

All passwords are in the format: `passwordXXX`

## Technology Stack

- **Backend:** Spring Boot 3.5.5
- **Template Engine:** Thymeleaf
- **Database:** MySQL 9.4
- **ORM:** JPA/Hibernate
- **Migration:** Flyway
- **CSS Framework:** Bootstrap 5
- **Build Tool:** Maven
- **Mapping:** MapStruct
- **Utilities:** Lombok

## Project Structure

```
src/main/
├── java/com/example/demo/
│   ├── controllers/
│   │   ├── UserController.java      # REST API
│   │   ├── UserUIController.java    # Web UI
│   │   └── HomeController.java
│   ├── entities/
│   │   ├── User.java
│   │   ├── Address.java
│   │   └── Profile.java
│   ├── repositories/
│   ├── services/
│   ├── mappers/
│   └── dtos/
└── resources/
    ├── templates/
    │   └── users/
    │       ├── list.html    # User list page
    │       ├── form.html    # Create/Edit form
    │       └── view.html    # User details
    ├── db/migration/
    │   ├── V1__initial_migration.sql
    │   ├── V2__add_state_column.sql
    │   ├── V3__move_state_from_users_to_addresses.sql
    │   ├── V4__add_profile_table.sql
    │   └── V5__add_sample_data.sql
    └── application.yaml
```

## Documentation

For detailed documentation, see [FRONTEND_DOCUMENTATION.md](FRONTEND_DOCUMENTATION.md)

## Database Configuration

Default configuration (in `application.yaml`):
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/store?createDatabaseIfNotExist=true
    username: root
    password: P@ssword
```

## Development

### Hot Reload
The application uses Spring Boot DevTools for automatic restart on code changes.

### Maven Commands
```bash
# Compile
mvnw.cmd compile

# Run tests
mvnw.cmd test

# Clean and build
mvnw.cmd clean package

# Run application
mvnw.cmd spring-boot:run

# Database migration
mvnw.cmd flyway:migrate

# Database clean
mvnw.cmd flyway:clean
```

## API Examples

### Get All Users
```bash
curl http://localhost:8080/users
```

### Get User by ID
```bash
curl http://localhost:8080/users/1
```

### Create User
```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"New User","email":"new@example.com","password":"pass123"}'
```

### Update User
```bash
curl -X PUT http://localhost:8080/users/1 \
  -H "Content-Type: application/json" \
  -d '{"name":"Updated Name","email":"updated@example.com"}'
```

### Delete User
```bash
curl -X DELETE http://localhost:8080/users/1
```

## Screenshots

### User List Page
- Clean table with all users
- Sort by name, email, or ID
- Action buttons for each user

### Create/Edit Form
- Simple, validated form
- Password field only for creation
- Cancel and submit buttons

### User Details Page
- Complete user information
- Avatar with first letter of name
- Quick actions (Edit, Delete)
- API endpoint documentation

## License

This project is for educational purposes.

## Support

For issues or questions, please refer to:
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [Bootstrap Documentation](https://getbootstrap.com/)

---

**Last Updated:** October 24, 2025

