# GitHub Projects dengan Multi-Auth (Gmail OAuth + SSO Institutional)

## Situasi Anda
Ingin support:
- ✅ Gmail/Google OAuth
- ✅ UNDIP.ac.id (SSO Institutional) atau akun Microsoft
- ✅ Multiple authentication methods

---

## 1. KEYCLOAK (RECOMMENDED UNTUK SSO INSTITUTIONAL)
**⭐ Paling Comprehensive | Stack: Java/Spring + React/Vue**

https://github.com/keycloak/keycloak

### Fitur:
- ✅ OAuth2, OIDC, SAML 2.0
- ✅ Google Sign-In
- ✅ Microsoft/Azure AD integration
- ✅ LDAP (untuk UNDIP.ac.id atau institutional directory)
- ✅ SSO federation (gabung multiple provider)
- ✅ Admin console built-in
- ✅ User management

### Setup:
```bash
# Download Keycloak
wget https://github.com/keycloak/keycloak/releases/download/[version]/keycloak-[version].tar.gz
tar xzf keycloak-*.tar.gz
cd keycloak-*/bin

# Start Keycloak
./kc.sh start-dev  # development mode

# Access admin console
# http://localhost:8080/admin
# Default: admin / admin (set first time)
```

### Configure Google OAuth di Keycloak:
1. Buka admin console: http://localhost:8080/admin
2. Menu: Realm > Identity Providers
3. Add Provider: Google
4. Set:
   - Client ID: (dari Google Cloud Console)
   - Client Secret: (dari Google Cloud Console)
5. Redirect URI: `http://localhost:8080/realms/master/broker/google/endpoint`

### Configure LDAP/UNDIP.ac.id:
1. Menu: Realm > User Federation
2. Add Provider: LDAP
3. Set LDAP URL: `ldap://ldap.undip.ac.id:389` (contoh)
4. Base DN: `dc=undip,dc=ac,dc=id`
5. Bind DN: `cn=admin,dc=undip,dc=ac,dc=id`

### Integrasi dengan Aplikasi Anda (Spring Boot):
```xml
<!-- pom.xml -->
<dependency>
  <groupId>org.keycloak</groupId>
  <artifactId>keycloak-spring-boot-starter</artifactId>
  <version>latest</version>
</dependency>
```

```yaml
# application.yml
keycloak:
  realm: master
  auth-server-url: http://localhost:8080
  ssl-required: external
  resource: your-app
  public-client: true
  credentials:
    secret: your-client-secret
```

---

## 2. IDENTITY SERVER 4 (.NET)
**⭐ Enterprise-grade | Stack: .NET Core + React/Angular**

https://github.com/DuendeSoftware/IdentityServer

### Fitur:
- ✅ OpenID Connect, OAuth2
- ✅ SAML 2.0
- ✅ Azure AD integration
- ✅ Google OAuth
- ✅ Social login providers
- ✅ Multi-tenancy

### Untuk Microsoft/UNDIP:
```csharp
// Startup.cs
services.AddIdentityServer()
  .AddGoogleConnect(new GoogleConnectOptions
  {
    ClientId = "YOUR_GOOGLE_ID",
    ClientSecret = "YOUR_GOOGLE_SECRET"
  })
  .AddAzureAdConnect(new AzureAdConnectOptions
  {
    Domain = "undip.onmicrosoft.com",
    TenantId = "YOUR_TENANT_ID",
    ClientId = "YOUR_CLIENT_ID",
    ClientSecret = "YOUR_CLIENT_SECRET"
  });
```

---

## 3. SPRING SECURITY + SPRING OAUTH2 + SAML2
**⭐ Cocok untuk Java/Spring | Stack: Spring Boot + Angular**

https://github.com/spring-projects/spring-security

### Support:
- OAuth2 (Google, GitHub, Facebook)
- SAML 2.0 (untuk UNDIP/institutional)
- OpenID Connect

### Setup Multi-Provider di Spring Boot:

```xml
<!-- pom.xml -->
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-oauth2-client</artifactId>
</dependency>
<dependency>
  <groupId>org.springframework.security</groupId>
  <artifactId>spring-security-saml2-service-provider</artifactId>
</dependency>
```

```yaml
# application.yml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: YOUR_GOOGLE_ID
            client-secret: YOUR_GOOGLE_SECRET
            scope: profile,email
          
          # SAML untuk UNDIP (contoh)
          undip-saml:
            provider: undip
            client-name: UNDIP SSO
            
    saml2:
      relyingparty:
        registration:
          undip:
            assertingparty:
              metadata-uri: https://sso.undip.ac.id/metadata.xml
              # atau
              # entity-id: https://sso.undip.ac.id
              # sso-url: https://sso.undip.ac.id/sso
              # certificate: ...
```

### Backend Endpoint untuk Multi-Login:

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    // Login dengan Google OAuth
    @GetMapping("/google-login")
    public void googleLogin() {
        // Spring Security handles this
    }
    
    // Login dengan UNDIP SAML
    @GetMapping("/undip-login")
    public void undipLogin() {
        // Spring Security SAML2 handles this
    }
    
    // Unified endpoint - semua user setelah login
    @GetMapping("/user")
    public UserInfo getUser(@AuthenticationPrincipal OAuth2User oAuth2User,
                            @AuthenticationPrincipal Saml2AuthenticatedPrincipal saml2User) {
        if (oAuth2User != null) {
            return new UserInfo(
                oAuth2User.getAttribute("email"),
                oAuth2User.getAttribute("name"),
                "GOOGLE"
            );
        }
        if (saml2User != null) {
            return new UserInfo(
                saml2User.getFirstAttribute("email"),
                saml2User.getFirstAttribute("displayName"),
                "UNDIP_SSO"
            );
        }
        return null;
    }
}
```

---

## 4. ORY KRATOS + HYDRA
**⭐ Modern & API-first | Stack: Go + React**

https://github.com/ory/kratos
https://github.com/ory/hydra

### Fitur:
- ✅ OAuth2, OpenID Connect
- ✅ SAML 2.0
- ✅ Google Sign-In
- ✅ Institutional SSO
- ✅ API-first approach (cocok untuk microservices)

### Setup:

```bash
git clone https://github.com/ory/kratos
cd kratos

# Configure OIDC providers
# Edit kratos/contrib/quickstart/kratos/oidc.jsonnet

cat > kratos/oidc.jsonnet <<EOF
{
  "google": {
    "client_id": "YOUR_GOOGLE_ID",
    "client_secret": "YOUR_GOOGLE_SECRET",
    "scope": ["profile", "email"],
    "mapper_url": "https://accounts.google.com/.well-known/openid-configuration"
  },
  "undip": {
    "client_id": "YOUR_UNDIP_CLIENT_ID",
    "client_secret": "YOUR_UNDIP_CLIENT_SECRET",
    "issuer_url": "https://sso.undip.ac.id",
    "mapper_url": "https://sso.undip.ac.id/.well-known/openid-configuration"
  }
}
EOF

docker-compose up
```

---

## 5. SHIBBOLETH (UNTUK INSTITUTIONAL SSO)
**⭐ Standard di Universitas | Stack: Java + Apache**

https://github.com/shibboleth/

### Fitur:
- ✅ SAML 2.0 federation
- ✅ LDAP integration
- ✅ Multi-institution SSO
- ✅ Standard di universitas di seluruh dunia (termasuk UNDIP kemungkinan)

### Cocok untuk UNDIP karena:
- UNDIP mungkin sudah pakai Shibboleth
- Anda bisa integrate langsung dengan UNDIP infrastructure
- Standard untuk academic institution

### Implementasi:
```bash
# Install Shibboleth Service Provider
sudo apt-get install libapache2-mod-shib

# Configure Apache
<Location /Shibboleth.sso>
  Allow all
</Location>

# Protect aplikasi Anda
<Location /secure>
  AuthType shibboleth
  ShibRequireSession On
  require valid-user
</Location>
```

---

## 6. ZITADEL (Modern Alternative)
**⭐ Cloud-native | Stack: Go + React**

https://github.com/zitadel/zitadel

### Fitur:
- ✅ OAuth2, OIDC, SAML
- ✅ Google, GitHub, social login
- ✅ Enterprise SSO
- ✅ Multi-tenancy
- ✅ Built-in user management

---

## PERBANDINGAN UNTUK KEBUTUHAN ANDA

| Project | Google OAuth | SAML/SSO | LDAP | Ease | Rekomendasi |
|---------|--------------|----------|------|------|------------|
| Keycloak | ✅ | ✅✅ | ✅✅ | Medium | ⭐⭐⭐⭐⭐ |
| IdentityServer | ✅ | ✅ | ✅ | Medium | ⭐⭐⭐ |
| Spring Security | ✅ | ✅ | Manual | Easy | ⭐⭐⭐⭐ |
| Ory Kratos | ✅ | ✅ | ✅ | Hard | ⭐⭐⭐ |
| Shibboleth | ✅ | ✅✅ | ✅✅ | Hard | ⭐⭐ (institutional) |
| Zitadel | ✅ | ✅ | ✅ | Medium | ⭐⭐⭐⭐ |

---

## REKOMENDASI UNTUK ANDA

### Opsi 1: Gunakan KEYCLOAK (RECOMMENDED)
**Alasan:**
- Paling mudah untuk setup multi-provider
- Sudah support Google OAuth out-of-box
- SAML/LDAP untuk UNDIP bisa di-configure via UI
- Admin console untuk manage users
- Bisa run standalone atau integrated dengan Spring Boot

**Setup:**
1. Jalankan Keycloak
2. Configure Google Provider di admin console
3. Configure UNDIP SSO di admin console
4. Spring Boot app hanya perlu client library Keycloak

### Opsi 2: Extend Aplikasi Anda (Spring Security)
**Alasan:**
- Tidak perlu server terpisah
- Langsung di Spring Boot Anda
- Lebih control

**Setup:**
1. Add Spring Security SAML2 dependency
2. Configure OAuth2 untuk Google
3. Configure SAML2 untuk UNDIP
4. Update SecurityConfig

### Opsi 3: Hybrid Approach
- Gunakan Keycloak untuk UNDIP/institutional SSO
- Spring Boot app integrate dengan Keycloak
- Frontend bisa pilih: login via Google atau UNDIP

---

## IMPLEMENTASI STEP-BY-STEP UNTUK APLIKASI ANDA

### Langkah 1: Pilih Approach (Keycloak atau Spring Security)

### Langkah 2: Update Backend
```java
// SecurityConfig.java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
          .authorizeHttpRequests(authz -> authz
            .requestMatchers("/api/public/**").permitAll()
            .anyRequest().authenticated()
          )
          .oauth2Login(oauth2 -> oauth2
            .authorizationEndpoint(authEndpoint -> authEndpoint
              .authorizationRequestRepository(cookieAuthorizationRequestRepository())
            )
            .tokenEndpoint(tokenEndpoint -> tokenEndpoint
              .accessTokenResponseClient(accessTokenResponseClient())
            )
          )
          .saml2Login(saml2 -> saml2
            .relyingPartyRegistrationRepository(relyingPartyRegistrationRepository())
          );
        
        return http.build();
    }
}
```

### Langkah 3: Update Frontend
```typescript
// login.component.ts
export class LoginComponent {
  ngOnInit() {
    this.loadLoginOptions();
  }
  
  loginOptions = [
    {
      name: 'Gmail',
      url: '/oauth2/authorization/google',
      icon: 'google-icon'
    },
    {
      name: 'UNDIP SSO',
      url: '/saml2/authenticate/undip',
      icon: 'undip-icon'
    }
  ];
  
  login(provider: string) {
    window.location.href = this.getLoginUrl(provider);
  }
}
```

```html
<!-- login.html -->
<div class="login-options">
  <button *ngFor="let option of loginOptions" 
          (click)="login(option.name)">
    <img [src]="'assets/' + option.icon + '.png'">
    {{ option.name }}
  </button>
</div>
```

---

## NEXT STEPS

1. **Tentukan apakah UNDIP sudah punya SSO**
   - Hub ke IT UNDIP
   - Minta metadata/config URL
   - Tanya protokol (SAML/OIDC/OAuth)

2. **Pilih architecture**
   - Standalone Keycloak: lebih mudah, terpisah
   - Direct Spring: lebih simple, integrated

3. **Implementasi**
   - Test dengan Google OAuth dulu
   - Setup UNDIP SSO setelah
   - Test multi-provider flow

---

## RESOURCES

- Keycloak Docs: https://www.keycloak.org/documentation
- Spring Security OAuth2: https://spring.io/projects/spring-security-oauth2-client
- Spring SAML2: https://docs.spring.io/spring-security/reference/servlet/saml2/index.html
- UNDIP IT Contact: `https://sso.undip.ac.id` (check jika ada)

---

## CONTOH METADATA SAML2

Untuk konfigurasi UNDIP SSO, Anda perlu metadata XML seperti ini:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<EntityDescriptor xmlns="urn:oasis:names:tc:SAML:2.0:metadata">
  <SPSSODescriptor protocolSupportEnumeration="urn:oasis:names:tc:SAML:2.0:protocol">
    <AssertionConsumerService 
      Binding="urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST"
      Location="http://localhost:8080/login/saml2/sso/undip"
      index="0" />
    <KeyDescriptor use="signing">
      <KeyInfo xmlns="http://www.w3.org/2000/09/xmldsig#">
        <X509Data>
          <X509Certificate>...</X509Certificate>
        </X509Data>
      </KeyInfo>
    </KeyDescriptor>
  </SPSSODescriptor>
</EntityDescriptor>
```

Minta file ini dari IT UNDIP untuk setup SSO mereka!
