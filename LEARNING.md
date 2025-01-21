# Week 1
- Install the necessities: JDK 17, Tomcat, IntelliJ, Postman
- Introduction to RESTful API development with Spring Boot (Modern day BE, RESTful API, JSON data type)
- [Spring Initializr](https://start.spring.io/index.html)
- Training git
- Set up Spring project (Main, pom.xml, application.properties)
- Basic Spring: Annotation, SpringApplication, RestController, RequestMapping (Get, Post, Put, Delete), ResponseEntity, handle exceptions (ControllerAdvice + ExceptionHandler)
### Set up git repo:
```shell
# Initialize a git repository at local machine
git init

# Config "origin" for the git repository
# Go to Github create an empty repository and paste into the <url>
git remote add origin <url>

# Config developer's identity in this project
git config user.name "<your name>"
git config user.email "<your email>"

# Create a "main" branch and checkout to it
git checkout -b main

# Add and commit all files
git add .
git commit -m "init"

# Push the main branch to Github
git push -u origin main
```
### Basic git commands:
```shell
# Stage all changes
git add .

# Commit the changes
# Read more here for conventional commits: https://www.conventionalcommits.org/en/v1.0.0/
git commit -m "<message>"

# List all the branches (local branches) and view the current branch
git branch

# List all the remote branches (branches on Github)
git branch -r

# Create a new branch from the current branch and checkout to the newly created branch
git checkout -b <branch name>

# Merge <branch name> into the current branch
git merge <branch name>
```
### Remember when using git:
- DON'T code on main/master (default) branch => Always create a branch from the default branch
- Always update code from git. Here are the steps:
```shell
# 1. Save your current code
git add .
git stash

# 2. Checkout to default branch (main for example)
git checkout main

# 3. Pull the new code
git pull

# 4. Checkout to your branch (longlt for example)
git checkout longlt

# 5. Merge the code from main to your branch
git merge main

# 6. Pop your code
git stash pop
``` 
- How to merge code (for repo manager):

For example, you are the repository manager. You have 2 pull requests (PR/A and PR/B) that needs to be merged into main branch.
You should merge the pull requests base on the priority of them. If the priority is same across pull requests, you should merge the earliest to the latest. Steps to merge all pull requests:
```shell
# 1. Go to Github and merge the first pull request (example: PR/A)
# 2. Go to local machine and pull the newest code from main
git checkout main
git pull

# 3. Checkout to the PR/B branch
git checkout PR/B

# 4. Merge the code from main to PR/B and resolve merge conflicts (if any)
git merge main

# 5. Push the PR/B again
git push

# 6. Go to Github and merge PR/B to main
# 7. Go to local machine and verify the merged code
git checkout main
git pull

# You code should be merged and updated
```
**Warning:** I recommend using "merge" instead of "rebase" because it is more safe. Use rebase without strong knowledge of git you will eventually lose your code.
### Code
#### Spring Application (Main)
```java
@SpringBootApplication
public class QltcApplication {

	public static void main(String[] args) {
		SpringApplication.run(QltcApplication.class, args);
	}

}
```
#### Rest Controller
```java
@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping("/")
    protected ResponseEntity getHello() throws Exception {
        return new ResponseEntity(new HashMap<String, Object>() {{ put("message", "Hello World!"); }}, HttpStatus.OK);
    }

    @PostMapping("/sum")
    protected ResponseEntity sum(@RequestBody SumDto dto) {
        Integer result = dto.getA() + dto.getB();
        return new ResponseEntity(new HashMap<String, Object>() {{ put("result", result); }}, HttpStatus.CREATED);
    }
}
```
#### Handle Exception
```java
@ControllerAdvice
public class QltcControllerAdvisor {
    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleException(Exception e) {
        e.printStackTrace();
        return new ResponseEntity(new HashMap<String, Object>() {{ put("message", e.getMessage()); }}, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
```