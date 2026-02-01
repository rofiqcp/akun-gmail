# Gmail Automation - User Login Tracking Setup

## Summary
✅ Backend and Frontend server running
✅ User login tracking system implemented  
✅ Email list endpoint created

## Running the Server

### Start servers:
```bash
bash /root/otomasi/gmail/server.sh start
# or
bash /root/otomasi/gmail/server.sh
```

### Other commands:
```bash
bash /root/otomasi/gmail/server.sh stop      # Stop both services
bash /root/otomasi/gmail/server.sh restart   # Restart both services  
bash /root/otomasi/gmail/server.sh status    # Check status
```

## Access Points

- **Frontend**: http://localhost:4200/login
- **Backend API**: http://localhost:8080/api

## How to Check Who Logged In

### Option 1: API Endpoint (Terminal)
```bash
curl -s http://localhost:8080/api/auth/users
```

Response:
```json
{
  "success": true,
  "count": 1,
  "users": [
    {
      "id": 1,
      "email": "sirobo.id@gmail.com",
      "name": "sirobo robot edukasi",
      "picture": "https://lh3.googleusercontent.com/a/ACg8ocJsuovO_jPJ3hMpkqWaRqPb88P2a4PAVw1c0ORfF6CM"
    }
  ]
}
```

### Option 2: Dashboard (Web UI)
1. Open http://localhost:4200/login
2. Click "Login dengan Google"
3. Select your Google account
4. After login, you'll see the dashboard with:
   - Your account info (Name, Email)
   - List of all users who have logged in
   - User count displayed in header

## What Happens on Login

When a user logs in via Google OAuth:

1. ✅ Google ID token is sent to backend
2. ✅ JWT is decoded to extract email, name, picture
3. ✅ User data is saved to database (H2 in-memory)
4. ✅ User is stored/updated in `users` table
5. ✅ Session token is created
6. ✅ User redirected to dashboard
7. ✅ Dashboard shows all users who have logged in

## Key Files

- Backend: `/root/otomasi/gmail/backend-spring/src/main/java/com/example/controller/AuthenticationController.java`
  - `POST /api/auth/google` - Login endpoint (saves user)
  - `GET /api/auth/users` - List all logged-in users

- Frontend: `/root/otomasi/gmail/frontend-angular/src/app/`
  - `login/` - Google OAuth login page
  - `dashboard/` - Shows logged-in users list

- Database: H2 in-memory database  
  - Table: `users` (id, email, name, picture)

## Example Usage

### Terminal command to list users:
```bash
curl -s http://localhost:8080/api/auth/users | python3 -m json.tool
```

### Check specific number of logins:
```bash
# Count total users
curl -s http://localhost:8080/api/auth/users | grep '"count"'

# Output: "count":3
```

## Notes

- User data automatically saved when they log in
- Each user email is unique (updated if same email logs in again)
- Can see full user details: email, name, profile picture
- Dashboard accessible at: http://localhost:4200/dashboard (after login)
