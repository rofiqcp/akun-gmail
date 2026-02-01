# UNDIP SSO Integration Guide

## ✅ UNDIP SSO Infrastructure Found!

**URL:** https://sso.undip.ac.id/
**Type:** Custom SSO system (bukan standard SAML/OAuth)
**Status:** Active & Working

---

## 📋 UNDIP SSO System Info

### Login Flow
1. User masuk ke https://sso.undip.ac.id/auth/user/login
2. Input: NIP/NIM/username/e-mail official UNDIP
3. Password (LDAP authenticated)
4. Session token created

### Available Endpoints
- **Login Page:** https://sso.undip.ac.id/auth/user/login
- **API Base:** https://sso.undip.ac.id/ (custom endpoints)
- **LDAP Backed:** Username/NIM/NIP authentication

### Technologies Detected
- Backend: Likely Java/PHP
- Frontend: Bootstrap-based UI
- Auth Method: LDAP + Custom session management
- Maintained by: TIM IT BAPSI (UNDIP IT Team)

---

## 🚀 INTEGRATION OPTIONS UNTUK APLIKASI ANDA

### OPTION 1: Direct LDAP Integration (RECOMMENDED)
**Hubungi IT UNDIP untuk credentials:**
- LDAP Server: ldap://ldap.undip.ac.id:389 (atau port lain)
- Base DN: dc=undip,dc=ac,dc=id
- Bind DN: cn=admin,dc=undip,dc=ac,dc=id (atau sesuai)
- LDAP password

**Implementasi di Spring Boot:**

```xml
<!-- pom.xml -->
<dependency>
  <groupId>org.springframework.ldap</groupId>
  <artifactId>spring-ldap-core</artifactId>
</dependency>
```

```yaml
# application.yml
spring:
  ldap:
    urls: ldap://ldap.undip.ac.id:389
    base: dc=undip,dc=ac,dc=id
    username: cn=admin,dc=undip,dc=ac,dc=id
    password: ${LDAP_PASSWORD}
```

```java
// LdapAuthenticationProvider.java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(authz -> authz
            .requestMatchers("/login/**").permitAll()
            .anyRequest().authenticated()
          )
          .formLogin(form -> form
            .loginPage("/login")
            .defaultSuccessUrl("/dashboard")
          )
          .authenticationProvider(ldapAuthenticationProvider());
        
        return http.build();
    }
    
    @Bean
    public LdapAuthenticationProvider ldapAuthenticationProvider() {
        return new LdapAuthenticationProvider(
          new BindAuthenticator(contextSource()),
          new LdapUserDetailsService()
        );
    }
    
    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        contextSource.setUrl("ldap://ldap.undip.ac.id:389");
        contextSource.setBase("dc=undip,dc=ac,dc=id");
        contextSource.setUserDn("cn=admin,dc=undip,dc=ac,dc=id");
        contextSource.setPassword("${LDAP_PASSWORD}");
        contextSource.afterPropertiesSet();
        return contextSource;
    }
}
```

### OPTION 2: OAuth2/OIDC Proxy (Jika UNDIP support)
**Hubungi IT UNDIP apakah mereka support:**
- OAuth2 endpoint?
- OIDC (OpenID Connect)?
- Client ID & Secret untuk aplikasi Anda?

Jika ada:
```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          undip:
            client-id: YOUR_APP_CLIENT_ID
            client-secret: YOUR_APP_CLIENT_SECRET
            authorization-grant-type: authorization_code
            redirect-uri: http://localhost:8080/login/oauth2/code/undip
            scope: profile,email
        provider:
          undip:
            authorization-uri: https://sso.undip.ac.id/oauth2/authorize
            token-uri: https://sso.undip.ac.id/oauth2/token
            user-info-uri: https://sso.undip.ac.id/oauth2/userinfo
            user-name-attribute: sub
```

### OPTION 3: Custom Integration (Jika UNDIP tidak support standard)
**Check UNDIP SSO API documentation atau reverse-engineer:**

```java
// CustomUndipAuthService.java
@Service
public class UndipAuthService {
    
    private static final String SSO_LOGIN_URL = "https://sso.undip.ac.id/auth/user/login";
    private static final String SSO_VERIFY_URL = "https://sso.undip.ac.id/verify/token";
    
    public UndipUser authenticate(String username, String password) {
        // 1. Send login request ke UNDIP SSO
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(SSO_LOGIN_URL);
        
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        
        request.setEntity(new UrlEncodedFormEntity(params));
        HttpResponse response = client.execute(request);
        
        // 2. Parse response & extract session token
        String sessionToken = extractSessionToken(response);
        
        // 3. Verify token dengan UNDIP
        HttpGet verifyRequest = new HttpGet(SSO_VERIFY_URL + "?token=" + sessionToken);
        HttpResponse verifyResponse = client.execute(verifyRequest);
        
        // 4. Return user data
        return parseUserData(verifyResponse);
    }
}
```

---

## 🔧 IMPLEMENTATION STEPS

### Step 1: Contact IT UNDIP
Email: bapsi@undip.ac.id (atau sesuai contact)

**Minta informasi:**
```
Halo IT UNDIP,

Kami ingin mengintegrasikan UNDIP SSO ke aplikasi web kami.
Mohon informasi berikut:

1. LDAP Server details:
   - LDAP URL
   - Base DN
   - Bind DN
   - Credentials untuk integration

2. Atau, apakah UNDIP support:
   - OAuth2 API?
   - OIDC (OpenID Connect)?
   - SAML 2.0?
   - Credentials untuk aplikasi kami?

3. User attributes yang kami butuh:
   - NIP/NIM
   - Email
   - Full Name
   - Department

Terima kasih!
```

### Step 2: Choose Integration Method
- Berdasarkan response IT UNDIP, pilih option 1, 2, atau 3

### Step 3: Implement Backend
- Setup authentication provider (LDAP/OAuth/Custom)
- Add user endpoint
- Add logout endpoint

### Step 4: Update Frontend
```typescript
// login.component.ts
export class LoginComponent {
  loginOptions = [
    {
      name: 'Gmail',
      url: '/oauth2/authorization/google',
      icon: 'google'
    },
    {
      name: 'UNDIP SSO',
      url: '/auth/undip',
      icon: 'undip'
    }
  ];
  
  loginUndip() {
    // Trigger UNDIP login
    window.location.href = '/auth/undip';
  }
}
```

```html
<!-- login.html -->
<div class="login-options">
  <button (click)="loginGoogle()">Login dengan Gmail</button>
  <button (click)="loginUndip()">Login dengan UNDIP SSO</button>
</div>
```

### Step 5: Test
```bash
# Test dengan credential UNDIP
curl -X POST http://localhost:8080/api/auth/undip-login \
  -H "Content-Type: application/json" \
  -d '{"username":"nimanda","password":"password123"}'
```

---

## 📊 CURRENT APPLICATION STATUS

### Aplikasi Anda Sekarang:
✅ Gmail OAuth login: WORKING
❌ UNDIP SSO: NOT YET

### Setelah Integration:
✅ Gmail OAuth login: WORKING
✅ UNDIP SSO login: WORKING
✅ User login tracking: WORKING

---

## 💻 QUICK IMPLEMENTATION (LDAP Option)

Jika IT UNDIP beri LDAP credentials, tambah ke aplikasi:

**1. Update pom.xml:**
```xml
<dependency>
  <groupId>org.springframework.ldap</groupId>
  <artifactId>spring-ldap-core</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-ldap</artifactId>
</dependency>
```

**2. Create UndipUserService:**
```java
@Service
public class UndipUserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public User loadUndipUser(String username) {
        // Load user dari LDAP via Spring LDAP
        // Save ke database jika belum ada
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setSource("UNDIP_LDAP");
            user = userRepository.save(user);
        }
        return user;
    }
}
```

**3. Update AuthenticationController:**
```java
@PostMapping("/auth/undip-login")
public ResponseEntity<?> undipLogin(@RequestBody LoginRequest request) {
    try {
        // LDAP authentication via Spring Security
        Authentication auth = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
            request.getUsername(),
            request.getPassword()
          )
        );
        
        // Load/create user
        User user = undipUserService.loadUndipUser(request.getUsername());
        
        // Save login history
        LoginHistory history = new LoginHistory();
        history.setUser(user);
        history.setLoginTime(LocalDateTime.now());
        history.setSource("UNDIP_LDAP");
        loginHistoryRepository.save(history);
        
        return ResponseEntity.ok(new AuthResponse(
          "Login successful",
          user.getUsername(),
          user.getEmail()
        ));
    } catch (BadCredentialsException e) {
        return ResponseEntity.status(401).body("Invalid credentials");
    }
}
```

---

## 📧 CONTACT INFO

**IT UNDIP (BAPSI):**
- Email: bapsi@undip.ac.id
- Phone: (024) xxxx-xxxx
- Website: http://bapsi.undip.ac.id

**SSO Website:** https://sso.undip.ac.id/

---

## 📝 NEXT ACTIONS

1. ✅ **Done:** Confirm UNDIP SSO exists
2. ⏳ **TODO:** Contact IT UNDIP untuk credentials
3. ⏳ **TODO:** Get LDAP/OAuth details
4. ⏳ **TODO:** Implement integration
5. ⏳ **TODO:** Test dengan account UNDIP
6. ⏳ **TODO:** Deploy to production

---

## CHECKLIST UNTUK CONVERSATION DENGAN IT UNDIP

- [ ] Confirm mereka punya LDAP server
- [ ] Get LDAP connection details (URL, port, base DN)
- [ ] Get bind credentials
- [ ] Confirm user attributes available
- [ ] Test LDAP connection sebelum implementasi
- [ ] Ask untuk testing account
- [ ] Ask untuk production credentials
- [ ] Ask untuk any IP whitelist requirements
- [ ] Get SLA/support contact

Sekarang Anda sudah tau UNDIP punya SSO! Tinggal hubungi IT mereka untuk integration details.
