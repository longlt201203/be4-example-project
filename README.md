# Week 2
- Install new libraries:
  - Lombok: auto getter/setter/constructor
  - JDBC API
  - Spring Data JPA
  - MS SQL Server Driver
```xml
<!-- pom.xml -->

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<dependency>
<groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jdbc</artifactId>
</dependency>

<dependency>
    <groupId>com.microsoft.sqlserver</groupId>
    <artifactId>mssql-jdbc</artifactId>
    <scope>runtime</scope>
</dependency>

<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>
```
### Using Lombok

**Remember to: install & enable Lombok plugins**
```java
@Data
public class SumDto {
    private Integer a;
    private Integer b;
}
```
- `@Data`: getter + setter
- `@Setter`: setter only
- `@Getter`: getter only
- `@NoArgsConstructor`: `public SumDto()`
- `@AllArgsConstructor`: `public SumDto(Integer a, Integer b)`
### Config Database
Update `application.properties`:
```properties
...
spring.datasource.username=sa
spring.datasource.password=Admin@123
spring.datasource.url=jdbc:sqlserver://localhost:1433;TrustServerCertificate=true;database=QltcDb

# Auto update database structure base on entity
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=update
```
### Code JPA Entity
```java
@Entity
@Data
public class HelloEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Double score;
}
```
When you run the program again, the database will be updated base on your code. Note: if your remove a column or table, the database will not delete/drop it.
### Code JPA Repository
```java
public interface HelloRepository extends JpaRepository<HelloEntity, Integer> {
}
```
Done! 😀

Custom function:
```java
public interface HelloRepository extends JpaRepository<HelloEntity, Integer> {
    boolean existsByName(String name);
}
```
DONE! 😲😲😲
### Using JPA Repository in Controller
```java
@RestController
@RequestMapping("/hello")
public class HelloController {
    @Autowired
    private HelloRepository helloRepository;

    ... old code

    @GetMapping("/check-name-exists")
    protected ResponseEntity checkNameExists(@RequestParam String name) {
        boolean isExisted = helloRepository.existsByName(name);
        return new ResponseEntity(new HashMap<String, Object>() {{ put("result", isExisted); }}, HttpStatus.OK);
    }

    @PostMapping("/create")
    protected ResponseEntity createHello(@RequestBody HelloEntity entity) {
        helloRepository.save(entity);
        return new ResponseEntity(new HashMap<String, Object>() {{ put("message", "Success!"); }}, HttpStatus.CREATED);
    }
}
```
