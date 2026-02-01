# Email Verification System Implementation

## ✅ Features Implemented

### Backend (Spring Boot)
- ✅ User model dengan email verification fields
- ✅ Email verification token generation (24-hour expiry)
- ✅ Endpoint untuk send verification email
- ✅ Endpoint untuk verify email token
- ✅ Check verification status endpoint
- ✅ Integration dengan JavaMailSender (Gmail SMTP)

### Database Changes
```sql
ALTER TABLE users ADD COLUMN email_verified BOOLEAN DEFAULT false;
ALTER TABLE users ADD COLUMN verification_token VARCHAR(255);
ALTER TABLE users ADD COLUMN verification_token_expires_at TIMESTAMP;
ALTER TABLE users ADD COLUMN created_at TIMESTAMP;
```

---

## 🚀 API ENDPOINTS

### 1. Send Verification Email
```bash
curl -X POST http://localhost:8080/api/auth/send-verification-email \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@gmail.com",
    "applicationUrl": "http://localhost:4200"
  }'
```

Response:
```json
{
  "success": true,
  "message": "Verification email sent to user@gmail.com"
}
```

### 2. Verify Email with Token
```bash
curl -X GET "http://localhost:8080/api/auth/verify-email?token=YOUR_TOKEN"
```

Response:
```json
{
  "success": true,
  "message": "Email verified successfully!"
}
```

### 3. Check Verification Status
```bash
curl -X GET "http://localhost:8080/api/auth/check-verification?email=user@gmail.com"
```

Response:
```json
{
  "success": true,
  "email": "user@gmail.com",
  "emailVerified": true,
  "name": "User Name"
}
```

---

## 📧 EMAIL CONFIGURATION

### Setup Gmail SMTP (For Email Sending)

#### Step 1: Enable 2FA di Google Account
1. Go to https://myaccount.google.com/
2. Security → 2-Step Verification (enable if not yet)

#### Step 2: Create App Password
1. Go to https://myaccount.google.com/apppasswords
2. Select "Mail" and "Windows Computer"
3. Copy the 16-character password

#### Step 3: Configure Backend

**Option A: Environment Variables**
```bash
export MAIL_USERNAME=your-email@gmail.com
export MAIL_PASSWORD=your-app-password

# Then start backend
cd /root/otomasi/gmail && bash /root/otomasi/gmail/server.sh start
```

**Option B: Direct in application.yml**
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password-16-chars
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
```

**Option C: Docker (Future)**
```bash
docker run -e MAIL_USERNAME=your@gmail.com \
           -e MAIL_PASSWORD=app-password \
           gmail-backend
```

### Alternative Email Providers

#### SendGrid
```yaml
spring:
  mail:
    host: smtp.sendgrid.net
    port: 587
    username: apikey
    password: SG.YOUR_SENDGRID_API_KEY
```

#### Mailgun
```yaml
spring:
  mail:
    host: smtp.mailgun.org
    port: 587
    username: postmaster@YOUR_DOMAIN.mailgun.org
    password: YOUR_MAILGUN_PASSWORD
```

#### AWS SES
```yaml
spring:
  mail:
    host: email-smtp.REGION.amazonaws.com
    port: 587
    username: YOUR_SMTP_USERNAME
    password: YOUR_SMTP_PASSWORD
```

---

## 🎯 INTEGRATION FLOW

### After User Login (Gmail or UNDIP):

```
1. User login dengan Gmail / UNDIP
   ↓
2. User data saved ke database
   ↓
3. emailVerified = false initially
   ↓
4. Backend send verification email ke user
   ↓
5. Email contains link: http://localhost:4200/verify-email?token=XXX
   ↓
6. User klik link
   ↓
7. Frontend hit endpoint: /api/auth/verify-email?token=XXX
   ↓
8. Backend verify token & mark user.emailVerified = true
   ↓
9. User dapat full access ke website
```

---

## 💻 FRONTEND INTEGRATION

### Update Dashboard Component:

```typescript
export class DashboardComponent implements OnInit {
  user: any = null;
  emailVerified: boolean = false;
  showVerificationPrompt: boolean = false;
  
  ngOnInit() {
    const userStr = localStorage.getItem('user');
    if (userStr) {
      this.user = JSON.parse(userStr);
      this.checkVerification();
    } else {
      this.router.navigate(['/login']);
    }
  }
  
  checkVerification() {
    fetch(`${environment.apiUrl}/auth/check-verification?email=${this.user.email}`)
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          this.emailVerified = data.emailVerified;
          if (!this.emailVerified) {
            this.showVerificationPrompt = true;
          }
        }
      });
  }
  
  sendVerificationEmail() {
    fetch(`${environment.apiUrl}/auth/send-verification-email`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        email: this.user.email,
        applicationUrl: window.location.origin
      })
    })
    .then(res => res.json())
    .then(data => {
      if (data.success) {
        alert('Verification email sent! Check your inbox.');
      }
    });
  }
}
```

### Create Verify Email Page:

```typescript
// verify-email.component.ts
export class VerifyEmailComponent implements OnInit {
  message: string = 'Verifying email...';
  isSuccess: boolean = false;
  
  constructor(private route: ActivatedRoute, private router: Router) {}
  
  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      const token = params['token'];
      if (token) {
        this.verifyEmail(token);
      }
    });
  }
  
  verifyEmail(token: string) {
    fetch(`${environment.apiUrl}/auth/verify-email?token=${token}`)
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          this.isSuccess = true;
          this.message = data.message;
          setTimeout(() => this.router.navigate(['/dashboard']), 3000);
        } else {
          this.message = data.message || 'Verification failed';
          this.isSuccess = false;
        }
      });
  }
}
```

---

## 📊 DATABASE SCHEMA

```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(255) UNIQUE NOT NULL,
  name VARCHAR(255),
  picture VARCHAR(255),
  email_verified BOOLEAN DEFAULT false,
  verification_token VARCHAR(255) UNIQUE,
  verification_token_expires_at TIMESTAMP,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_email ON users(email);
CREATE INDEX idx_verification_token ON users(verification_token);
```

---

## 🧪 TESTING

### Manual Test Flow:

```bash
# 1. Simulate user login (save user)
curl -X POST http://localhost:8080/api/auth/google \
  -H "Content-Type: application/json" \
  -d '{
    "token": "fake-token",
    "email": "test@example.com",
    "name": "Test User",
    "picture": "https://example.com/pic.jpg"
  }'

# 2. Check verification status (should be false)
curl http://localhost:8080/api/auth/check-verification?email=test@example.com

# 3. Send verification email
curl -X POST http://localhost:8080/api/auth/send-verification-email \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "applicationUrl": "http://localhost:4200"
  }'

# 4. Get token dari email atau database
# SELECT verification_token FROM users WHERE email='test@example.com';

# 5. Verify dengan token
curl "http://localhost:8080/api/auth/verify-email?token=ACTUAL_TOKEN"

# 6. Check status again (should be true)
curl http://localhost:8080/api/auth/check-verification?email=test@example.com
```

---

## ⚙️ CONFIGURATION

### application.yml
```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: ${MAIL_USERNAME:your-email@gmail.com}
    password: ${MAIL_PASSWORD:your-app-password}
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          connectiontimeout: 5000
          timeout: 5000
          writetimeout: 5000
  
  jpa:
    hibernate:
      ddl-auto: create  # Change to 'update' for production

app:
  url: http://localhost:4200  # Change for production
```

---

## 📝 EMAIL TEMPLATE

Current email template (simple):
```
Hello,

Please click the link below to verify your email:

http://localhost:4200/verify-email?token=XXX

This link will expire in 24 hours.

Best regards,
Gmail Automation Team
```

### Custom Template (Optional):
Bisa customize dengan HTML template di `EmailService.java`:

```java
String emailBody = "<html>" +
  "<body>" +
  "<h1>Welcome!</h1>" +
  "<p>Please verify your email:</p>" +
  "<a href='" + verificationLink + "'>Verify Email</a>" +
  "<p>Or copy this link: " + verificationLink + "</p>" +
  "</body>" +
  "</html>";

MimeMessage message = mailSender.createMimeMessage();
MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
helper.setText(emailBody, true);  // true = HTML
```

---

## 🚨 TROUBLESHOOTING

### Email not sending?

**1. Check Gmail credentials**
```bash
# Verify SMTP connection
telnet smtp.gmail.com 587

# Test with swaks (if available)
swaks --to test@example.com --from your@gmail.com \
      --server smtp.gmail.com:587 --auth LOGIN \
      --auth-user your@gmail.com --auth-password app-password
```

**2. Check app-password is valid**
```bash
# Re-generate app-password at:
https://myaccount.google.com/apppasswords
```

**3. Check logs**
```bash
# View backend logs
tail -f /root/otomasi/gmail/.run/backend.log
```

### Token expired?

Currently set to 24 hours. Change in `UserService.java`:
```java
user.setVerificationTokenExpiresAt(LocalDateTime.now().plusHours(24));  // Change 24 to needed hours
```

### Multiple verification emails?

Implement rate limiting:
```java
public void sendVerificationEmail(User user, String applicationUrl) {
  // Check if email was sent recently (within 5 minutes)
  if (user.getLastVerificationEmailSentAt() != null && 
      user.getLastVerificationEmailSentAt().isAfter(LocalDateTime.now().minusMinutes(5))) {
    throw new Exception("Verification email already sent. Try again later.");
  }
  
  // ... send email ...
  user.setLastVerificationEmailSentAt(LocalDateTime.now());
}
```

---

## 📚 NEXT FEATURES

- [ ] Resend verification email endpoint
- [ ] Bulk email verification for testing
- [ ] HTML email templates
- [ ] Custom email branding
- [ ] Email logs/history tracking
- [ ] Integration dengan email service providers (SendGrid, Mailgun, SES)
- [ ] 2FA with email OTP
- [ ] Email notification preferences
