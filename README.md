# Week 3
- Data Transformation Flow
![](./data%20transformation%20flow.png)
- Validation library
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```
- Project structure:
```
<main package> (com.be4.qltc)
    | QltcApplication.java
    | utils
        | ...
    | modules
        | database
            | entities
                | SampleEntity.java
                | ...
            | repositories
                | SampleRepository.java
                | ...
        | sample
            | dto
                | SampleRequestDto.java
                | SampleResponseDto.java
            | SampleController.java
            | SampleService.java
            | ...
```
- CRUD Controller
- `@Service` indicates a service
```java
@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    ...
}
```
- Import service into controller using `@Autowired`
```java
@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    ...
}
```