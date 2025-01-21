# Week 4
## Config spring security
- Spring security library
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```
- Add configuration package
```
utils
    | configs
        | QltcConfiguration.java
        | QltcSecurityConfiguration.java
    | ...
```
- Basic configuration:
```java
@Configuration
@EnableWebSecurity
public class QltcSecurityConfiguration {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return http.build();
    }
}
```
## Login
![](./basic%20authentication.png)
- Auth module
```
modules
    | auth
        | ...
```
- Go to [jwt.io](https://jwt.io/) to read about JWT. Library:
```xml
<dependency>
  <groupId>com.auth0</groupId>
  <artifactId>java-jwt</artifactId>
  <version>4.4.0</version>
</dependency>
```
- Update `application.properties`:
```properties
...

qltc.jwt.secret=thisisverysecret
qltc.jwt.issuer=localhost:8080
```
- Update `QltcConfiguration.java`:
```java
@Configuration
@Import(QltcSecurityConfiguration.class)
@Data
public class QltcConfiguration {
    @Value("${qltc.jwt.secret}")
    private String jwtSecret;

    @Value("${qltc.jwt.issuer}")
    private String jwtIssuer;
}
```
- Add `BasicLoginDto`:
```java
@Data
public class BasicLoginDto {
    private String email;
    private String password;
}
```
- Add `AuthService`:
```java
@Service
public class AuthService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private QltcConfiguration configuration;

    public String basicLogin(BasicLoginDto dto) {
        Optional<AccountEntity> result = accountRepository.findByEmail(dto.getEmail());
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password!");
        }
        AccountEntity account = result.get();
        if (!dto.getPassword().equals(account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password!");
        }
        Instant now = Instant.now();
        Algorithm algorithm = Algorithm.HMAC256(configuration.getJwtSecret());
        return JWT.create()
                .withSubject(account.getAccountId().toString())
                .withIssuer(configuration.getJwtIssuer())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(30, ChronoUnit.DAYS))
                .sign(algorithm);
    }
}
```
- Add `AuthController`:
```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/basic")
    private ResponseEntity basicLogin(@RequestBody BasicLoginDto dto) {
        String accessToken = authService.basicLogin(dto);
        return new ResponseEntity(new HashMap<String, Object>() {{ put("accessToken", accessToken); }}, HttpStatus.CREATED);
    }
}
```
## Verify token
- Update `AuthService`:
```java
@Service
public class AuthService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private QltcConfiguration configuration;

    ...

    public Optional<AccountEntity> verifyAccessToken(String accessToken) {
        Algorithm algorithm = Algorithm.HMAC256(configuration.getJwtSecret());
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer(configuration.getJwtIssuer())
                .build()
                .verify(accessToken);
        Integer accountId = Integer.valueOf(decodedJWT.getSubject());
        Optional<AccountEntity> result = accountRepository.findById(accountId);
        return result;
    }
}
```
- Implement `Authority`:
```java
// utils/QltcAuthority

public class QltcAuthority implements GrantedAuthority {
    public static QltcAuthority of(Integer role) {
        return new QltcAuthority(role.toString());
    }

    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

    private QltcAuthority(String authority) {
        this.authority = authority;
    }
}
```
- Implement `Authentication`:
```java
// utils/QltcAuthentication

public class QltcAuthentication implements Authentication {
    private AccountEntity account;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account != null ? List.of(QltcAuthority.of(account.getRole())) : List.of();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return account;
    }

    @Override
    public boolean isAuthenticated() {
        return account != null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }

    @Override
    public String getName() {
        return AccountEntity.class.getName();
    }

    public QltcAuthentication(AccountEntity account) {
        this.account = account;
    }
}
```
- Add `QltcSecurityFilter`:
```java
public class QltcSecurityFilter extends OncePerRequestFilter {
    @Autowired
    private AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        SecurityContext context = SecurityContextHolder.getContext();
        QltcAuthentication authentication = null;
        try {
            String accessToken = getAccessToken(request);
            Optional<AccountEntity> result = authService.verifyAccessToken(accessToken);
            authentication = new QltcAuthentication(result.get());
        } catch (JWTVerificationException | NullPointerException e) {
            // Do nothing
        }
        context.setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

    private String getAccessToken(HttpServletRequest request) {
        String at = null;
        String authorization = request.getHeader("Authorization");
        String[] parts = authorization.split(" ");
        if (parts.length == 2 && parts[0].equals("Bearer")) {
            at = parts[1];
        }
        return at;
    }
}
```
- Update `QltcSecurityConfig`:
```java
@Configuration
@EnableWebSecurity
public class QltcSecurityConfiguration {
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public QltcSecurityFilter qltcSecurityFilter() {
        return new QltcSecurityFilter();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/auth/profile")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/accounts/")
                        .permitAll()
                        .requestMatchers("/api/accounts/**")
                        .hasAnyAuthority(QltcAuthority.of(1).getAuthority())
                        .requestMatchers("/api/auth/**")
                        .permitAll()
                        .anyRequest()
                        .denyAll()
                )
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(qltcSecurityFilter(), AuthorizationFilter.class);
        return http.build();
    }
}
```
- Get profile API:
```java
// AuthController

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    ...

    @GetMapping("/profile")
    private ResponseEntity getProfile() {
        AccountEntity profile = authService.getProfile();
        return new ResponseEntity(AccountResponseDto.fromEntity(profile), HttpStatus.OK);
    }
}
```
```java
// AuthService

@Service
public class AuthService {
    ...

    public AccountEntity getProfile() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (AccountEntity) context.getAuthentication().getPrincipal();
    }
}
```
## Hash password
- Update `CreateAccountDto`:
```java
public class CreateAccountRequestDto {
    ...

    public AccountEntity toEntity() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return new AccountEntity(email, phone, encoder.encode(password), fname, lname, 0, avt);
    }
}
```
- Update `UpdateAccountDto`:
```java
@Data
public class UpdateAccountRequestDto {
    ...
    // Remove the password field. We will separate the change password feature to another API

    public AccountEntity toEntity(AccountEntity prevEntity) {
        return new AccountEntity(prevEntity.getAccountId(), email, phone, prevEntity.getPassword(), fname, lname, 0, avt);
    }
}
```
- Remember to increate the length of the password field in database:
```java
// AccountEntity.java

@Column(length = 255, nullable = false)
private String password;
```
- Update `basicLogin` in `AuthService`:
```java
...

AccountEntity account = result.get();
BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
if (!encoder.matches(dto.getPassword(), account.getPassword())) {
    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password!");
}
Instant now = Instant.now();
Algorithm algorithm = Algorithm.HMAC256(configuration.getJwtSecret());

...
```