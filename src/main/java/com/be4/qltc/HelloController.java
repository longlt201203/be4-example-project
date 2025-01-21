package com.be4.qltc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/hello")
public class HelloController {
    @Autowired
    private HelloRepository helloRepository;

    @GetMapping("/")
    protected ResponseEntity getHello() throws Exception {
        return new ResponseEntity(new HashMap<String, Object>() {{ put("message", "Hello World!"); }}, HttpStatus.OK);
    }

    @PostMapping("/sum")
    protected ResponseEntity sum(@RequestBody SumDto dto) {
        Integer result = dto.getA() + dto.getB();
        return new ResponseEntity(new HashMap<String, Object>() {{ put("result", result); }}, HttpStatus.CREATED);
    }

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
