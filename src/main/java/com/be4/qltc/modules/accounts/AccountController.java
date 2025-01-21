package com.be4.qltc.modules.accounts;

import com.be4.qltc.modules.accounts.dto.AccountResponseDto;
import com.be4.qltc.modules.accounts.dto.CreateAccountRequestDto;
import com.be4.qltc.modules.accounts.dto.UpdateAccountRequestDto;
import com.be4.qltc.modules.database.entities.AccountEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/")
    private ResponseEntity createAccount(@Valid @RequestBody CreateAccountRequestDto dto) {
        AccountEntity entity = accountService.createAccount(dto);
        return new ResponseEntity(AccountResponseDto.fromEntity(entity), HttpStatus.CREATED);
    }

    @GetMapping("/")
    private ResponseEntity listAccounts() {
        List<AccountEntity> list = accountService.listAccounts();
        return new ResponseEntity(AccountResponseDto.fromEntities(list), HttpStatus.OK);
    }

    @GetMapping("/{accountId}")
    private ResponseEntity getAccount(@Valid @PathVariable Integer accountId) {
        AccountEntity entity = accountService.getAccount(accountId);
        return new ResponseEntity(AccountResponseDto.fromEntity(entity), HttpStatus.OK);
    }

    @PutMapping("/{accountId}")
    private ResponseEntity updateAccount(@Valid @PathVariable Integer accountId, @Valid @RequestBody UpdateAccountRequestDto dto) {
        AccountEntity entity = accountService.updateAccount(accountId, dto);
        return new ResponseEntity(AccountResponseDto.fromEntity(entity), HttpStatus.OK);
    }

    @DeleteMapping("/{accountId}")
    private ResponseEntity deleteAccount(@Valid @PathVariable Integer accountId) {
        AccountEntity entity = accountService.deleteAccount(accountId);
        return new ResponseEntity(AccountResponseDto.fromEntity(entity), HttpStatus.OK);
    }
}
