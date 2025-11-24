# User Management System

![CI Status](https://github.com/ielkabani/demo2/workflows/CI%20-%20Build%20and%20Test/badge.svg)

A complete Spring Boot application with REST APIs, authentication, and a modern web interface built with Thymeleaf and Bootstrap 5.

## Quick Start

### Prerequisites
- Java 17
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

4. **Login:**
   - Email: `john.doe@example.com`
   - Password: `password123`

## Features

### Authentication & Security
- ✅ User login with email and password
- ✅ Session-based authentication
- ✅ Password change functionality
- ✅ User activation/deactivation
- ✅ Protected routes (requires login)
- ✅ Logout functionality

### Web UI
- ✅ Login page with authentication
- ✅ View all users with sorting
- ✅ Create new user
- ✅ Edit user information
- ✅ View user details
- ✅ Delete user
- ✅ Change password
- ✅ Responsive design (mobile-friendly)
- ✅ Flash messages for actions
- ✅ Session management

### REST API
- ✅ GET `/users` - List all users
- ✅ GET `/users/{id}` - Get user by ID
- ✅ POST `/users` - Create new user
- ✅ PUT `/users/{id}` - Update user
- ✅ DELETE `/users/{id}` - Delete user
- ✅ POST `/users/{id}/change-password` - Change password

### Business Logic (Service Layer)
- ✅ User authentication with validation
- ✅ Password strength validation
- ✅ User state management (active/inactive)
- ✅ Custom exception handling
- ✅ Business rule enforcement

## Sample Data

The application comes with 5 sample users (all active):
1. John Doe - `john.doe@example.com` / `password123`
2. Jane Smith - `jane.smith@example.com` / `password456`
3. Alice Johnson - `alice.johnson@example.com` / `password789`
4. Bob Williams - `bob.williams@example.com` / `password321`
5. Charlie Brown - `charlie.brown@example.com` / `password654`

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

## Application Routes

### Public Routes
- `GET /` - Redirects to login or user list (if logged in)
- `GET /login` - Login page
- `POST /login` - Authenticate user

### Protected Routes (Requires Login)
- `GET /ui/users` - User list page
- `GET /ui/users/new` - Create user form
- `GET /ui/users/{id}` - View user details
- `GET /ui/users/{id}/edit` - Edit user form
- `POST /ui/users` - Create user
- `POST /ui/users/{id}` - Update user
- `POST /ui/users/{id}/delete` - Delete user
- `GET /change-password` - Change password page
- `POST /change-password` - Change password
- `GET /logout` - Logout

## Exception Handling

The application includes custom exceptions for business logic:

- **InvalidCredentialsException** - Thrown when login credentials are incorrect
- **InvalidUserStateException** - Thrown when user account is inactive
- **UserNotFoundException** - Thrown when user is not found
- **WeakPasswordException** - Thrown when password doesn't meet requirements

## Password Requirements

- Minimum 8 characters
- At least one uppercase letter
- At least one lowercase letter
- At least one digit
- At least one special character (@$!%*?&)

## Project Structure

```
src/main/
├── java/com/example/demo/
│   ├── controllers/
│   │   ├── HomeController.java          # Authentication & password management
│   │   ├── UserController.java          # REST API endpoints
│   │   └── UserUIController.java        # Web UI pages
│   ├── entities/
│   │   ├── User.java                    # User entity with addresses
│   │   ├── Address.java                 # Address entity
│   │   └── Profile.java                 # User profile entity
│   ├── repositories/
│   │   ├── UserRepository.java
│   │   ├── AddressRepository.java
│   │   └── ProfileRepository.java
│   ├── services/
│   │   └── UserService.java             # Business logic layer
│   ├── mappers/
│   │   └── UserMapper.java              # MapStruct mapper
│   ├── dtos/
│   │   ├── UserDto.java
│   │   ├── LoginRequest.java
│   │   └── ChangePasswordRequest.java
│   └── exceptions/
│       ├── InvalidCredentialsException.java
│       ├── InvalidUserStateException.java
│       ├── UserNotFoundException.java
│       └── WeakPasswordException.java
└── resources/
    ├── templates/
    │   ├── login.html                   # Login page
    │   ├── change-password.html         # Change password page
    │   └── users/
    │       ├── list.html                # User list page
    │       ├── form.html                # Create/Edit form
    │       └── view.html                # User details
    ├── static/css/
    │   ├── common.css                   # Common styles
    │   ├── form.css                     # Form styles
    │   ├── list.css                     # List page styles
    │   └── view.css                     # View page styles
    ├── db/migration/
    │   ├── V1__initial_migration.sql
    │   ├── V2__add_state_column.sql
    │   ├── V3__move_state_from_users_to_addresses.sql
    │   ├── V4__add_profile_table.sql
    │   ├── V5__add_sample_data.sql
    │   └── V6__add_active_column.sql
    └── application.yaml
```

## How to Use the Application

### 1. First Time Setup
- Start the application
- Navigate to `http://localhost:8080`
- You'll be redirected to the login page

### 2. Login
- Use any of the sample user credentials (e.g., `john.doe@example.com` / `password123`)
- Click "Login"
- You'll be redirected to the user list page

### 3. Manage Users
- **View all users**: Click on "Users" in the navigation
- **Create new user**: Click "Add New User" button
- **View details**: Click "View" button next to any user
- **Edit user**: Click "Edit" button next to any user
- **Delete user**: Click "Delete" button next to any user

### 4. Change Password
- Click "Change Password" in the navigation
- Enter your old password
- Enter a new password (must meet requirements)
- Click "Change Password"

### 5. Logout
- Click "Logout" in the navigation

## Service Layer Architecture

The application demonstrates a clean separation of concerns with a service layer:

### UserService
Handles all business logic for user operations:

1. **Authentication (`login`)**
   - Validates user credentials
   - Checks if user account is active
   - Returns user data if successful
   - Throws exceptions for invalid credentials or inactive accounts

2. **Password Change (`changePassword`)**
   - Verifies old password
   - Validates new password strength
   - Updates password
   - Throws exceptions for invalid data

3. **Password Validation**
   - Enforces password strength requirements
   - Prevents weak passwords

### Exception Flow
```
Controller → Service → Repository
    ↓           ↓
Exception ← Exception
    ↓
Error Message → User
```

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

### Change Password
```bash
curl -X POST http://localhost:8080/users/1/change-password \
  -H "Content-Type: application/json" \
  -d '{"oldPassword":"password123","newPassword":"NewPass123@"}'
```

## Troubleshooting

### Port Already in Use
If you see "Port 8080 was already in use":
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F
```

Or change the port in `application.yaml`:
```yaml
server:
  port: 8081
```

### Database Connection Failed
1. Ensure MySQL is running on port 3306
2. Verify credentials in `application.yaml`
3. Check if database `store` exists (it will be created automatically)

### Flyway Migration Failed
Clean and re-migrate:
```bash
mvnw.cmd flyway:clean flyway:migrate
```

### Login Not Working
1. Ensure you're using one of the sample user emails
2. Password format is: `password123`, `password456`, etc.
3. Check that the user's `active` column is set to `true` in the database

### Session Expired
If redirected to login unexpectedly:
- Your session may have expired
- Simply log in again

## Learning Objectives

This application demonstrates:

1. **Spring Boot Architecture**
   - Controller-Service-Repository pattern
   - Dependency injection
   - Configuration management

2. **Web Development**
   - Thymeleaf templating
   - Form handling
   - Session management
   - Flash attributes

3. **Database Operations**
   - JPA/Hibernate ORM
   - Flyway migrations
   - Entity relationships

4. **Business Logic**
   - Service layer implementation
   - Exception handling
   - Input validation
   - Password security

5. **REST API Design**
   - RESTful endpoints
   - HTTP methods
   - JSON request/response

## Contributing

This is an educational project. Feel free to:
- Add new features
- Improve existing code
- Fix bugs
- Add tests
- Enhance documentation

## License

This project is for educational purposes.

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

