package com.be4.qltc.modules.auth;

import com.be4.qltc.modules.accounts.dto.AccountResponseDto;
import com.be4.qltc.modules.auth.dto.BasicLoginDto;
import com.be4.qltc.modules.database.entities.AccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

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

    @GetMapping("/profile")
    private ResponseEntity getProfile() {
        AccountEntity profile = authService.getProfile();
        return new ResponseEntity(AccountResponseDto.fromEntity(profile), HttpStatus.OK);
    }
}
