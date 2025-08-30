# cURL examples: Create Task with Spring Security (Basic Auth)

This app protects all endpoints with Spring Security. We configured HTTP Basic authentication and disabled CSRF checks for `/api/**` so that REST clients like curl can POST without a CSRF token.

Default credentials (see `src/main/resources/application.properties`):
- username: `admin`
- password: `secret`

Default port: `8080`.

Endpoint to create a task:
- POST `http://localhost:8080/api/tasks`
- Body (JSON):
  - `title` (string, required, max 255)
  - `description` (string, optional)
  - `dueDate` (string, ISO-8601 OffsetDateTime, optional), e.g. `2025-12-31T17:00:00Z`

Example JSON body:
```json
{
  "title": "Buy groceries",
  "description": "Milk, eggs, bread",
  "dueDate": "2025-12-31T17:00:00Z"
}
```

## Windows PowerShell

```powershell
$body = @{
  title = "Buy groceries"
  description = "Milk, eggs, bread"
  dueDate = "2025-12-31T17:00:00Z"
} | ConvertTo-Json

curl.exe -X POST "http://localhost:8080/api/tasks" `
  -H "Content-Type: application/json" `
  -u admin:secret `
  --data $body
```

## Bash / macOS / Linux

```bash
curl -X POST "http://localhost:8080/api/tasks" \
  -H "Content-Type: application/json" \
  -u admin:secret \
  -d '{
    "title": "Buy groceries",
    "description": "Milk, eggs, bread",
    "dueDate": "2025-12-31T17:00:00Z"
  }'
```

## Notes
- Make sure your database (MySQL by default) is running and the credentials in `application.properties` are correct.
- On success, the API returns `201 Created` with the created Task JSON and a `Location` header like `/api/tasks/{id}`.
- If you change `spring.security.user.name` or `spring.security.user.password`, update the `-u user:pass` part accordingly.
- If you prefer to keep CSRF enabled for APIs, switch to a token-based approach (e.g. OAuth2 Bearer tokens) or configure a `CookieCsrfTokenRepository` and include the token in your requests.


---

# cURL examples: Get All Tasks

Endpoint to list tasks:
- GET http://localhost:8080/api/tasks
- Optional query params:
  - status: PENDING | IN_PROGRESS | COMPLETED
  - dueBefore: ISO-8601 OffsetDateTime (e.g., 2025-12-31T17:00:00Z)

## Windows PowerShell

```powershell
# All tasks
curl.exe -X GET "http://localhost:8080/api/tasks" `
  -H "Accept: application/json" `
  -u admin:secret

# Filter by status
curl.exe -X GET "http://localhost:8080/api/tasks?status=PENDING" `
  -H "Accept: application/json" `
  -u admin:secret

# Filter by due date
curl.exe -X GET "http://localhost:8080/api/tasks?dueBefore=2025-12-31T17:00:00Z" `
  -H "Accept: application/json" `
  -u admin:secret
```

## Bash / macOS / Linux

```bash
# All tasks
curl -X GET "http://localhost:8080/api/tasks" \
  -H "Accept: application/json" \
  -u admin:secret

# Filter by status
curl -X GET "http://localhost:8080/api/tasks?status=PENDING" \
  -H "Accept: application/json" \
  -u admin:secret

# Filter by due date
curl -X GET "http://localhost:8080/api/tasks?dueBefore=2025-12-31T17:00:00Z" \
  -H "Accept: application/json" \
  -u admin:secret
```

## Notes
- All endpoints are protected by HTTP Basic; update -u if you changed credentials in application.properties.
- The list endpoint returns 200 OK with a JSON array of tasks.
