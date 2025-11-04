# Thymeleaf + Spring Boot Tutorial

## What is This Project?

This is a **simple, educational project** demonstrating Server-Side Rendering (SSR) with Thymeleaf and Spring Boot. The design is intentionally minimal to focus on core concepts rather than complex UI frameworks.

## Architecture Overview

```
┌─────────────┐
│   Browser   │
└──────┬──────┘
       │ HTTP Request
       ▼
┌─────────────────────┐
│  UserUIController   │ ← Handles UI requests (/ui/users)
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│   UserRepository    │ ← Data access
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│   MySQL Database    │
└──────┬──────────────┘
       │
       ▼
┌─────────────────────┐
│   Thymeleaf Engine  │ ← Renders HTML with data
└──────┬──────────────┘
       │ HTML Response
       ▼
┌─────────────┐
│   Browser   │ ← Displays rendered page
└─────────────┘
```

## Key Thymeleaf Concepts Demonstrated

### 1. **Variable Expressions** - `${...}`
Access data from the Model:
```html
<td th:text="${user.name}">John Doe</td>
```
- Gets the `name` property from the `user` object
- Fallback text "John Doe" shows during design

### 2. **Iteration** - `th:each`
Loop through collections:
```html
<tr th:each="user : ${users}">
    <td th:text="${user.id}">1</td>
    <td th:text="${user.name}">Name</td>
</tr>
```
- Iterates over the `users` list
- Creates one `<tr>` for each user

### 3. **Conditional Rendering** - `th:if`
Show/hide elements based on conditions:
```html
<div th:if="${successMessage}" class="message success">
    <span th:text="${successMessage}"></span>
</div>
```
- Only renders if `successMessage` exists
- Great for flash messages

### 4. **URL Building** - `@{...}`
Create URLs with path variables:
```html
<a th:href="@{/ui/users/{id}(id=${user.id})}">View</a>
```
- Builds URL: `/ui/users/5`
- Automatically handles encoding

### 5. **Form Binding** - `th:object` and `th:field`
Bind forms to objects:
```html
<form th:object="${user}" method="post">
    <input th:field="*{name}" type="text">
    <input th:field="*{email}" type="email">
</form>
```
- `th:object` sets the form object
- `*{name}` is shorthand for `${user.name}`
- Automatically sets `name`, `id`, and `value` attributes

### 6. **Selection Variable** - `*{...}`
Used with `th:object` for cleaner syntax:
```html
<form th:object="${user}">
    <input th:field="*{name}">  <!-- Same as ${user.name} -->
</form>
```

### 7. **Ternary Operator**
Conditional text/values:
```html
<h1 th:text="${isEdit} ? 'Edit User' : 'Create User'">Form</h1>
```
- If `isEdit` is true, shows "Edit User"
- Otherwise shows "Create User"

### 8. **String Operations**
Manipulate strings:
```html
<span th:text="${user.name.substring(0, 1).toUpperCase()}">J</span>
```
- Gets first character of name
- Converts to uppercase

## Project Structure

```
src/main/
├── java/com/example/demo/
│   ├── controllers/
│   │   ├── HomeController.java      ← Redirects / to /ui/users
│   │   ├── UserUIController.java    ← Handles UI requests
│   │   └── UserController.java      ← REST API (unchanged)
│   ├── entities/
│   │   └── User.java
│   ├── repositories/
│   │   └── UserRepository.java
│   └── dtos/
│       ├── RegisterUserRequest.java
│       └── UpdateUserRequest.java
│
└── resources/
    ├── application.yaml              ← Configuration
    └── templates/                    ← Thymeleaf templates
        └── users/
            ├── list.html            ← User list page
            ├── form.html            ← Create/Edit form
            └── view.html            ← User details page
```

## How It Works

### 1. **Request Flow - Viewing List**
```
Browser → GET /ui/users
    ↓
UserUIController.listUsers()
    ↓
userRepository.findAll()
    ↓
model.addAttribute("users", users)
    ↓
return "users/list"
    ↓
Thymeleaf renders templates/users/list.html
    ↓
HTML → Browser
```

### 2. **Request Flow - Creating User**
```
Browser → GET /ui/users/new
    ↓
UserUIController.showCreateForm()
    ↓
model.addAttribute("user", new RegisterUserRequest())
model.addAttribute("isEdit", false)
    ↓
return "users/form"
    ↓
Thymeleaf renders templates/users/form.html
    ↓
HTML form → Browser
    ↓
User fills form and submits
    ↓
Browser → POST /ui/users
    ↓
UserUIController.createUser()
    ↓
userRepository.save(user)
    ↓
redirectAttributes.addFlashAttribute("successMessage", "User created!")
    ↓
return "redirect:/ui/users"
    ↓
Browser redirects to list page with success message
```

### 3. **Request Flow - Updating User**
```
Browser → GET /ui/users/5/edit
    ↓
UserUIController.showEditForm(5)
    ↓
userRepository.findById(5)
    ↓
model.addAttribute("user", user)
model.addAttribute("userId", 5)
model.addAttribute("isEdit", true)
    ↓
return "users/form"
    ↓
Thymeleaf renders form with user data
    ↓
User modifies form and submits
    ↓
Browser → POST /ui/users/5 (with _method=PUT)
    ↓
Spring's HiddenHttpMethodFilter converts to PUT
    ↓
UserUIController.updateUser(5, request)
    ↓
userRepository.save(updatedUser)
    ↓
return "redirect:/ui/users/5"
```

### 4. **Request Flow - Viewing User**
```
Browser → GET /ui/users/{id}
    ↓
UserUIController.viewUser(id)
    ↓
userRepository.findById(id)
    ↓
model.addAttribute("user", user)
    ↓
return "users/view"
    ↓
Thymeleaf renders templates/users/view.html
    ↓
HTML → Browser
```

**Not-found flow (graceful handling):**
```
Browser → GET /ui/users/{id}
    ↓
UserUIController.viewUser(id)
    ↓
userRepository.findById(id) → empty
    ↓
redirectAttributes.addFlashAttribute("errorMessage", "User not found")
    ↓
return "redirect:/ui/users"
    ↓
Browser redirects to list page with error message
```

### 5. **Request Flow - Deleting User**
```
Browser → Form POST /ui/users/5 (with _method=DELETE)
    ↓
Spring converts to DELETE request
    ↓
UserUIController.deleteUser(5)
    ↓
userRepository.deleteById(5)
    ↓
return "redirect:/ui/users"
```

## Key Features Explained

### HTTP Method Override
HTML forms only support GET and POST. To use PUT and DELETE:

1. **Enable in application.yaml:**
```yaml
spring:
  mvc:
    hiddenmethod:
      filter:
        enabled: true
```

2. **Add hidden field in form:**
```html
<form method="post">
    <input type="hidden" name="_method" value="PUT">
    <!-- form fields -->
</form>
```

Spring's `HiddenHttpMethodFilter` reads `_method` and converts the request.

### Flash Attributes
Temporary data that survives a redirect:

```java
redirectAttributes.addFlashAttribute("successMessage", "User created!");
return "redirect:/ui/users";
```

Available in the next request only:
```html
<div th:if="${successMessage}" class="message success">
    <span th:text="${successMessage}"></span>
</div>
```

### Model Attributes
Data passed from controller to view:

```java
@GetMapping
public String listUsers(Model model) {
    model.addAttribute("users", userRepository.findAll());
    return "users/list";
}
```

Access in template:
```html
<tr th:each="user : ${users}">
    <!-- user data here -->
</tr>
```

## Running the Application

### 1. Start the Application
```bash
mvnw.cmd spring-boot:run
```

### 2. Access the UI
```
http://localhost:9090
```
(Redirects to http://localhost:9090/ui/users)

### 3. Available UI Routes
- `GET /` → Redirects to user list
- `GET /ui/users` → List all users
- `GET /ui/users/new` → Show create form
- `POST /ui/users` → Create user
- `GET /ui/users/{id}` → View user details
- `GET /ui/users/{id}/edit` → Show edit form
- `PUT /ui/users/{id}` → Update user (via POST + _method=PUT)
- `DELETE /ui/users/{id}` → Delete user (via POST + _method=DELETE)

### 4. REST API Still Available
The REST API endpoints are unchanged and still work:
- `GET /users` → JSON list
- `GET /users/{id}` → JSON user
- `POST /users` → Create via JSON
- `PUT /users/{id}` → Update via JSON
- `DELETE /users/{id}` → Delete via JSON

## Common Patterns

### Pattern 1: List Page
```java
@GetMapping
public String list(Model model) {
    model.addAttribute("items", repository.findAll());
    return "items/list";
}
```

```html
<tr th:each="item : ${items}">
    <td th:text="${item.name}"></td>
</tr>
```

### Pattern 2: Create Form
```java
@GetMapping("/new")
public String showForm(Model model) {
    model.addAttribute("item", new Item());
    return "items/form";
}

@PostMapping
public String create(@ModelAttribute Item item) {
    repository.save(item);
    return "redirect:/items";
}
```

```html
<form th:object="${item}" method="post">
    <input th:field="*{name}" type="text">
    <button type="submit">Save</button>
</form>
```

### Pattern 3: Edit Form
```java
@GetMapping("/{id}/edit")
public String showEditForm(@PathVariable Long id, Model model) {
    model.addAttribute("item", repository.findById(id).orElseThrow());
    model.addAttribute("isEdit", true);
    return "items/form";
}

@PutMapping("/{id}")
public String update(@PathVariable Long id, @ModelAttribute Item item) {
    repository.save(item);
    return "redirect:/items/" + id;
}
```

```html
<form th:action="${isEdit} ? '/items/' + ${itemId} : '/items'" method="post">
    <input th:if="${isEdit}" type="hidden" name="_method" value="PUT">
    <input th:field="*{name}" type="text">
    <button type="submit" th:text="${isEdit} ? 'Update' : 'Create'"></button>
</form>
```

## Learning Resources

### Official Documentation
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Spring Boot with Thymeleaf](https://spring.io/guides/gs/serving-web-content/)
- [Spring MVC Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc.html)

### Key Thymeleaf Attributes
- `th:text` - Sets element text content
- `th:utext` - Sets unescaped HTML content
- `th:if` - Conditional rendering
- `th:unless` - Inverse of th:if
- `th:each` - Iteration
- `th:href` - Creates links
- `th:src` - Sets src attribute
- `th:action` - Sets form action
- `th:object` - Sets form backing object
- `th:field` - Binds input to form object property
- `th:value` - Sets input value
- `th:attr` - Sets any attribute

### Expression Types
- `${...}` - Variable expressions (get data from model)
- `*{...}` - Selection expressions (relative to th:object)
- `@{...}` - URL expressions (build URLs)
- `#{...}` - Message expressions (i18n)
- `~{...}` - Fragment expressions (template fragments)

## Why This Design?

### Educational Focus
This project uses:
- ✅ Simple inline CSS instead of Bootstrap/Tailwind
- ✅ No JavaScript (pure server-side rendering)
- ✅ Clear, commented HTML
- ✅ Minimal dependencies

### What You Learn
1. **Server-Side Rendering** - How HTML is generated on the server
2. **Template Engines** - How Thymeleaf processes templates
3. **MVC Pattern** - Controller → Model → View flow
4. **Form Handling** - How forms submit and process data
5. **HTTP Methods** - GET, POST, PUT, DELETE
6. **Redirects** - Post-Redirect-Get pattern
7. **Flash Attributes** - Temporary session data

### Next Steps
After mastering these basics, you can:
1. Add JavaScript for interactivity
2. Use CSS frameworks (Bootstrap, Tailwind)
3. Implement fragments for reusable components
4. Add validation with Bean Validation
5. Implement authentication with Spring Security
6. Add pagination and search
7. Use AJAX for partial page updates

## Troubleshooting

### Template Not Found
**Error:** `TemplateNotFoundException: list`

**Fix:** Ensure template is in `src/main/resources/templates/users/list.html`

### Method Not Allowed
**Error:** `405 Method Not Allowed` when updating/deleting

**Fix:** Ensure hidden method filter is enabled:
```yaml
spring:
  mvc:
    hiddenmethod:
      filter:
        enabled: true
```

### Data Not Binding
**Error:** Form submits but data is null

**Fix:** Ensure matching names:
```java
public class User {
    private String name; // Must match form field
}
```

```html
<input th:field="*{name}"> <!-- Must match property name -->
```

---

**Happy Learning! 🚀**

