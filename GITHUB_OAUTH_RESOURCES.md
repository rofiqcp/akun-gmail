# GitHub Open-Source Gmail/Google OAuth Login Systems

Complete guide to well-documented Gmail/Google OAuth authentication projects with working frontend + backend examples.

---

## 1. **Spring Boot + React OAuth2 Social Login Demo** ⭐ 1.5K Stars

**Repository:** https://github.com/callicoder/spring-boot-react-oauth2-social-login-demo

**Description:**  
Complete OAuth2 social authentication system supporting Google, Facebook, and GitHub sign-in. Production-ready with MySQL database integration and user profile management.

**Tech Stack:**
- **Frontend:** React.js
- **Backend:** Spring Boot (Java)
- **Database:** MySQL
- **Security:** Spring Security, OAuth2
- **Additional:** JWT Authentication

**Setup Instructions:**

### Backend Setup:
```bash
# 1. Create MySQL database
mysql> create database spring_social

# 2. Configure database in application.yml
# File: spring-social/src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/spring_social?useSSL=false
    username: YOUR_DB_USERNAME
    password: YOUR_DB_PASSWORD

# 3. Add OAuth2 credentials
security:
  oauth2:
    client:
      registration:
        google:
          clientId: YOUR_GOOGLE_CLIENT_ID
          clientSecret: YOUR_GOOGLE_CLIENT_SECRET
          redirectUriTemplate: "{baseUrl}/oauth2/callback/{registrationId}"
          scope:
            - email
            - profile

# 4. Run the application
mvn spring-boot:run
```

### Frontend Setup:
```bash
cd react-social
npm install
npm start
```

**Special Features:**
- ✅ Multi-provider support (Google, Facebook, GitHub)
- ✅ User profile management
- ✅ Persistent login sessions
- ✅ Complete REST API
- ✅ Database user tracking
- ✅ Refresh token handling

**Google OAuth Configuration:**
1. Get credentials from: https://console.developers.google.com/
2. Redirect URI: `http://localhost:8080/oauth2/callback/google`
3. Required scopes: `email`, `profile`

---

## 2. **Spring Boot + Angular OAuth2 Social Login Demo** ⭐ 81 Stars

**Repository:** https://github.com/JavaChinna/spring-boot-angular-oauth2-social-login-demo

**Description:**  
Enterprise-grade authentication system with Angular frontend. Supports user registration, JWT authentication, and OAuth2 social login with Google, Facebook, LinkedIn, and GitHub.

**Tech Stack:**
- **Frontend:** Angular 10+
- **Backend:** Spring Boot 2+
- **Database:** JPA/Hibernate
- **Security:** Spring Security 5, OAuth2, JWT
- **Architecture:** RESTful API

**Setup Instructions:**

### Backend Setup:
```bash
cd spring-boot-oauth2-social-login

# Configure application.yml with your database
# Add OAuth2 credentials for each provider

# Run the application
mvn clean install
mvn spring-boot:run
```

### Frontend Setup:
```bash
cd angular-11-social-login
npm install
npm start
```

**Special Features:**
- ✅ User registration system
- ✅ JWT token generation
- ✅ Multiple OAuth2 providers (Google, Facebook, LinkedIn, GitHub)
- ✅ User profile persistence
- ✅ Email verification
- ✅ Comprehensive error handling
- ✅ Tutorial documentation included

**Documentation:**
- Part 1: Creating Backend REST API
- Part 2: Backend Continued
- Part 3: Angular Client Application
- Available at: www.javachinna.com (tutorial blog)

---

## 3. **Healthcare Appointment Scheduling App** ⭐ 144 Stars

**Repository:** https://github.com/Project-Based-Learning-IT/healthcare-appointment-scheduling-app

**Description:**  
Full-stack application with Google Sign-In for patient authentication. Includes appointment booking, calendar integration, payment processing, and feedback system. Production-deployed on Netlify & Render.

**Tech Stack:**
- **Frontend:** React.js
- **Backend:** Node.js, Express.js
- **Database:** MongoDB Atlas
- **Authentication:** Google Sign-In, JWT
- **Hosting:** Netlify (frontend), Render (backend)
- **Additional:** Calendar API, Payment integration

**Live Demo:**
- Frontend: https://healthcarebooking.netlify.app/
- Backend: https://healthcare-appointment-scheduling-app.onrender.com

**Setup Instructions:**

```bash
# Backend Setup
cd backend
npm install

# Configure environment variables
# - Google OAuth credentials
# - MongoDB connection string
# - JWT secret

npm start

# Frontend Setup
cd frontend
npm install

# Configure Google OAuth client ID in environment config

npm start
```

**Special Features:**
- ✅ Google Sign-In integration
- ✅ Doctor & Patient dual authentication
- ✅ Appointment slot booking with calendar
- ✅ Payment gateway integration
- ✅ Meeting link generation (Zoom/Meet)
- ✅ Feedback & rating system
- ✅ User profile management
- ✅ Email notifications
- ✅ Responsive UI

**User Management:**
- Patient: Google OAuth login, self-service profile
- Doctor: Manual registration + credentials login
- Dual-role tracking system

---

## 4. **Angular Social Login Library** ⭐ 656 Stars

**Repository:** https://github.com/abacritt/angularx-social-login

**Description:**  
Professional-grade Angular library for social authentication. Supports Google, Facebook, Microsoft, Amazon, and VK sign-in. Latest support for Angular 19+ with modern Google Identity Services.

**Tech Stack:**
- **Framework:** Angular 13-20+
- **Language:** TypeScript
- **Libraries:** Google Identity Services (gis), Facebook SDK
- **Architecture:** Module-based, dependency injection

**Installation:**
```bash
npm install @abacritt/angularx-social-login
```

**Setup Instructions:**

### 1. Import Module in AppModule:
```typescript
import { SocialLoginModule, SocialAuthServiceConfig } from '@abacritt/angularx-social-login';
import { GoogleLoginProvider, FacebookLoginProvider } from '@abacritt/angularx-social-login';

@NgModule({
  imports: [SocialLoginModule],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        lang: 'en',
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider('YOUR_GOOGLE_CLIENT_ID')
          },
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider('YOUR_FACEBOOK_CLIENT_ID')
          }
        ],
        onError: (err) => console.error(err)
      } as SocialAuthServiceConfig
    }
  ]
})
export class AppModule { }
```

### 2. Use in Component:
```typescript
import { SocialAuthService } from "@abacritt/angularx-social-login";
import { SocialUser } from "@abacritt/angularx-social-login";

@Component({
  selector: 'app-login',
  template: `
    <asl-google-signin-button type='standard' size='large'></asl-google-signin-button>
  `
})
export class LoginComponent implements OnInit {
  user: SocialUser;
  loggedIn: boolean;

  constructor(private authService: SocialAuthService) { }

  ngOnInit() {
    this.authService.authState.subscribe((user) => {
      this.user = user;
      this.loggedIn = (user != null);
      // Send auth token to backend
      if (this.loggedIn) {
        this.sendTokenToBackend(user.idToken);
      }
    });
  }

  signOut(): void {
    this.authService.signOut();
  }
}
```

### 3. Google Sign-In Button Styling:
```html
<asl-google-signin-button 
  type='standard' 
  size='large' 
  theme='outline'
  logo_alignment='left'>
</asl-google-signin-button>
```

**Special Features:**
- ✅ Modern Google Identity Services (gis) support
- ✅ One-tap sign-in capability
- ✅ Access token retrieval
- ✅ Token refresh handling
- ✅ Custom provider support
- ✅ Multiple scope configuration
- ✅ Type-safe TypeScript implementation
- ✅ Extensive documentation

**Supported Providers:**
- Google (with One-Tap)
- Facebook
- Microsoft
- Amazon
- VK (VKontakte)
- Custom providers

**Compatibility Matrix:**
- Angular 20: v2.5.X
- Angular 19: v2.4.X
- Angular 18: v2.3.X
- Angular 17: v2.2.X

---

## 5. **Django-Allauth (Python/Django)** ⭐ 10,288 Stars

**Repository:** https://github.com/pennersr/django-allauth

**Description:**  
Most popular Django authentication library with comprehensive OAuth2 support. Production-ready with extensive provider integrations and excellent documentation.

**Tech Stack:**
- **Framework:** Django
- **Language:** Python
- **Database:** Flexible (PostgreSQL, MySQL, SQLite)
- **Authentication:** OAuth2, SAML, Email/Password
- **Features:** Account management, registration, email verification

**Installation:**
```bash
pip install django-allauth
```

**Setup Instructions:**

### 1. Add to INSTALLED_APPS:
```python
INSTALLED_APPS = [
    'django.contrib.sites',
    'allauth',
    'allauth.account',
    'allauth.socialaccount',
    'allauth.socialaccount.providers.google',
]

SITE_ID = 1
```

### 2. Configure Google OAuth:
```python
# settings.py
SOCIALACCOUNT_PROVIDERS = {
    'google': {
        'SCOPE': [
            'profile',
            'email',
        ],
        'AUTH_PARAMS': {
            'access_type': 'online',
        },
        'VERIFIED_EMAIL': True,
        'APP': {
            'client_id': 'YOUR_GOOGLE_CLIENT_ID',
            'secret': 'YOUR_GOOGLE_CLIENT_SECRET',
            'key': ''
        }
    }
}
```

### 3. Add URLs:
```python
# urls.py
urlpatterns = [
    path('accounts/', include('allauth.urls')),
]
```

**Special Features:**
- ✅ 40+ OAuth providers
- ✅ Email/password authentication
- ✅ User registration with email verification
- ✅ Account management
- ✅ SAML support
- ✅ User profile data sync
- ✅ Token management
- ✅ User tracking and analytics
- ✅ Extensive documentation

---

## 6. **Additional Notable Projects**

### Keyist E-commerce (Spring Boot + Angular) ⭐ 371 Stars
**URL:** https://github.com/antkaynak/Keyist-Ecommerce
- OAuth2 + NGRX + MySQL
- E-commerce platform with social login

### Spring Boot 3 Microservices (with OAuth2) ⭐ 387 Stars
**URL:** https://github.com/SaiUpadhyayula/spring-boot-3-microservices-course
- Kubernetes, Kafka, OAuth2
- Complete microservices architecture

### Keycloak Security Example ⭐ 100 Stars
**URL:** https://github.com/wkrzywiec/keycloak-security-example
- OAuth2 with Keycloak identity provider
- Spring Boot + Angular integration

---

## Quick Comparison Table

| Project | Stars | Frontend | Backend | Database | Key Feature |
|---------|-------|----------|---------|----------|------------|
| Spring Boot + React OAuth2 | 1.5K | React | Spring Boot | MySQL | Multi-provider (Google, FB, GitHub) |
| Spring Boot + Angular OAuth2 | 81 | Angular 10 | Spring Boot 2 | JPA | JWT + OAuth2 combination |
| Healthcare App | 144 | React | Node.js/Express | MongoDB | Production-ready with payments |
| Angular Social Login Lib | 656 | Angular 19 | N/A (Library) | N/A | Modern Angular library |
| Django-Allauth | 10.3K | Django Templates | Django | Flexible | 40+ providers, most popular |

---

## Setup Checklist for Gmail/Google OAuth

1. **Get Google Credentials:**
   - Go to: https://console.developers.google.com/
   - Create new project
   - Enable Google+ API
   - Create OAuth2 credentials (Web Application)
   - Add authorized redirect URIs:
     - `http://localhost:3000/auth/google/callback` (React)
     - `http://localhost:4200/auth/google/callback` (Angular)
     - `http://localhost:8080/oauth2/callback/google` (Spring Boot)

2. **Copy Client ID & Secret**
   - Store securely in environment variables
   - Never commit to version control

3. **Configure Scope**
   - Minimum: `email`, `profile`
   - Optional: `calendar.readonly`, `drive.readonly`

4. **Test Locally**
   - Run backend on localhost
   - Run frontend on localhost
   - Test login flow end-to-end

5. **Deploy**
   - Update redirect URIs in Google Console
   - Use production domain
   - Secure with HTTPS

---

## Best Practices

✅ **Always use HTTPS** in production  
✅ **Store secrets** in environment variables  
✅ **Validate tokens** on backend  
✅ **Use refresh tokens** for long sessions  
✅ **Log authentication events** for audit trail  
✅ **Implement rate limiting** on login endpoints  
✅ **Hash user data** before database storage  
✅ **Keep dependencies updated**  

---

## Resources

- Google OAuth Documentation: https://developers.google.com/identity/protocols/oauth2
- GitHub OAuth Documentation: https://docs.github.com/en/developers/apps/building-oauth-apps
- JWT Best Practices: https://tools.ietf.org/html/rfc8725
- Spring Security OAuth2: https://spring.io/projects/spring-security-oauth

