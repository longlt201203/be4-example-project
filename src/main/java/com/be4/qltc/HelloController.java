package com.be4.qltc;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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
