package com.be4.qltc.modules.accounts;

import com.be4.qltc.modules.accounts.dto.CreateAccountRequestDto;
import com.be4.qltc.modules.accounts.dto.UpdateAccountRequestDto;
import com.be4.qltc.modules.database.entities.AccountEntity;
import com.be4.qltc.modules.database.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    public AccountEntity createAccount(CreateAccountRequestDto dto) {
        return accountRepository.save(dto.toEntity());
    }

    public List<AccountEntity> listAccounts() {
        return accountRepository.findAll();
    }

    public AccountEntity getAccount(Integer accountId) {
        Optional<AccountEntity> account = accountRepository.findById(accountId);
        if (account.isEmpty()) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found!");
        return account.get();
    }

    public AccountEntity updateAccount(Integer accountId, UpdateAccountRequestDto accountRequestDto) {
        getAccount(accountId);
        AccountEntity entity = accountRequestDto.toEntity(accountId);
        return accountRepository.save(entity);
    }

    public AccountEntity deleteAccount(Integer accountId) {
        AccountEntity entity = getAccount(accountId);
        accountRepository.delete(entity);
        return entity;
    }
}
