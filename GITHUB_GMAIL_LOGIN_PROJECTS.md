# GitHub Projects dengan Gmail Login System

## 1. Spring Boot + React OAuth2 Social Login (TOP REKOMENDASI)
**⭐ 1.5K Stars | Stack: React + Spring Boot + MySQL**

https://github.com/callicoder/spring-boot-react-oauth2-social-login-demo

### Fitur:
- ✅ Multi-provider: Google, Facebook, GitHub
- ✅ User registration & management
- ✅ JWT authentication
- ✅ Lengkap dengan frontend & backend

### Setup:
```bash
# Backend (Spring Boot)
git clone https://github.com/callicoder/spring-boot-react-oauth2-social-login-demo
cd spring-boot-react-oauth2-social-login-demo

# Config Google OAuth di application.properties
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET

mvn clean install
mvn spring-boot:run

# Frontend (React)
cd client
npm install
npm start
```

### Database Schema:
```sql
-- User table otomatis created
CREATE TABLE user (
  id BIGINT PRIMARY KEY,
  name VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  image_url VARCHAR(255),
  email_verified BOOLEAN,
  provider VARCHAR(50),
  provider_id VARCHAR(255)
);
```

---

## 2. Spring Boot + Angular OAuth2 Social Login
**⭐ 81 Stars | Stack: Angular 10+ + Spring Boot 2+**

https://github.com/JavaChinna/spring-boot-angular-oauth2-social-login-demo

### Fitur:
- ✅ Sama seperti project 1, tapi pakai Angular
- ✅ JWT token management
- ✅ Role-based access control (RBAC)
- ✅ Perfect untuk yang pakai Angular (seperti aplikasi Anda!)

### Setup:
```bash
git clone https://github.com/JavaChinna/spring-boot-angular-oauth2-social-login-demo
cd spring-boot-angular-oauth2-social-login-demo

# Backend
cd backend
mvn clean install
mvn spring-boot:run  # Runs on port 8080

# Frontend
cd ../frontend
npm install
npm start  # Runs on port 4200
```

### Konfigurasi:
**backend/src/main/resources/application.yml**
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_CLIENT_ID
            client-secret: YOUR_CLIENT_SECRET
            scope: profile,email
```

---

## 3. Healthcare Appointment Scheduling App
**⭐ 144 Stars | Stack: React + Node.js/Express + MongoDB**

https://github.com/Project-Based-Learning-IT/healthcare-appointment-scheduling-app

### Fitur:
- ✅ Google Sign-In integration
- ✅ Google Calendar API integration
- ✅ Payment processing (Stripe)
- ✅ Production-ready deployment
- ✅ Real-world use case

### Setup:
```bash
git clone https://github.com/Project-Based-Learning-IT/healthcare-appointment-scheduling-app
cd healthcare-appointment-scheduling-app

# Backend (Node.js)
cd server
npm install
npm start  # port 5000

# Frontend (React)
cd ../client
npm install
npm start  # port 3000
```

### User Model (MongoDB):
```javascript
const userSchema = new Schema({
  googleId: String,
  email: String,
  name: String,
  picture: String,
  displayName: String,
  createdAt: { type: Date, default: Date.now }
});
```

---

## 4. Angular Social Login Library (FOR FRONTEND)
**⭐ 656 Stars | Stack: Angular 13-20+ TypeScript**

https://github.com/abacritt/angularx-social-login

### Ini adalah library, bukan full app. Tapi sangat berguna untuk frontend!

### Instalasi:
```bash
npm install @abacritt/angularx-social-login
```

### Usage di Component:
```typescript
import { SocialAuthService } from '@abacritt/angularx-social-login';
import { GoogleLoginProvider } from '@abacritt/angularx-social-login';

export class LoginComponent {
  constructor(private authService: SocialAuthService) {}

  signInWithGoogle(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID);
  }
}
```

### Supported Providers:
- Google
- Facebook
- Microsoft
- Amazon
- LinkedIn
- GitHub

---

## 5. Django-Allauth (PYTHON)
**⭐ 10.3K Stars | Stack: Django + Python**

https://github.com/pennersr/django-allauth

### Fitur:
- ✅ Paling popular di Django community
- ✅ 40+ OAuth providers termasuk Google
- ✅ Social account management
- ✅ Email verification
- ✅ Multi-tenancy support

### Setup:
```bash
pip install django-allauth

# settings.py
INSTALLED_APPS = [
    ...
    'django.contrib.sites',
    'allauth',
    'allauth.account',
    'allauth.socialaccount',
    'allauth.socialaccount.providers.google',
]

ACCOUNT_EMAIL_REQUIRED = True
SOCIALACCOUNT_PROVIDERS = {
    'google': {
        'SCOPE': ['profile', 'email'],
        'APP': {'client_id': 'YOUR_CLIENT_ID'},
    }
}
```

---

## Perbandingan Project

| Project | Stack | Kompleksitas | Rekomendasi |
|---------|-------|--------------|------------|
| #1 Spring+React | React + Spring | Intermediate | ✅ Paling populer |
| #2 Spring+Angular | Angular + Spring | Intermediate | ✅ Seperti Anda! |
| #3 Healthcare App | Node+React | Advanced | ✅ Production-ready |
| #4 Angular Library | Angular | Easy | ✅ Untuk frontend |
| #5 Django-Allauth | Django | Easy-Intermediate | ✅ Python users |

---

## Langkah untuk Menggunakan

### Jika Anda pakai Java Spring + Angular:
1. Clone project #2 (paling match dengan setup Anda)
2. Copy struktur backend dari project tersebut
3. Adaptasi dengan aplikasi Anda

### Jika Anda pakai JavaScript:
1. Clone project #1 atau #3
2. Modifikasi sesuai kebutuhan

### Jika Anda sudah punya backend + frontend:
1. Gunakan project sebagai reference/learning material
2. Copy-paste bagian auth yang relevan
3. Adaptasi dengan database schema Anda

---

## Quick Integration Checklist

Untuk integrate Gmail login ke project Anda:

```bash
# 1. Clone template project
git clone [salah satu URL di atas]

# 2. Setup Google OAuth
- Buka: https://console.cloud.google.com
- Create project
- Setup OAuth 2.0 credentials
- Add redirect URIs: http://localhost:8080/login/oauth2/code/google

# 3. Copy auth files
- Copy AuthController.java dari template
- Copy login component dari template
- Customize sesuai kebutuhan

# 4. Update config
- Set Google Client ID di application.properties/yml
- Set database credentials

# 5. Build & Run
mvn clean install
mvn spring-boot:run
```

---

## Rekomendasi untuk Aplikasi Anda

**Berdasarkan setup Anda (Spring Boot + Angular):**

1. **Terbaik:** Clone project #2 (Spring Boot + Angular OAuth2)
   - Struktur sama dengan aplikasi Anda
   - Sudah terintegrasi dengan baik
   
2. **Gunakan sebagai reference:** Project #1 (Spring Boot + React)
   - Lebih populer dan well-maintained
   - Banyak star & contributor

3. **Untuk library:** Project #4
   - Upgrade Angular login component Anda
   - Add multi-provider support

---

## Useful Resources

- Google OAuth Documentation: https://developers.google.com/identity
- Spring Security OAuth2: https://spring.io/projects/spring-security-oauth
- Angular Social Login: https://github.com/abacritt/angularx-social-login

---

## File untuk Referensi

Dari setiap project, focus pada file ini:

**Backend:**
- `SecurityConfig.java` - OAuth2 configuration
- `AuthController.java` / `AuthService.java` - Auth endpoints
- `User.java` / `UserModel` - User entity

**Frontend:**
- `login.component.ts` - Login logic
- `auth.service.ts` - Auth service
- `app-routing.module.ts` - Route guards
