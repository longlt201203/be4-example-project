package com.be4.qltc.modules.accounts.dto;

import com.be4.qltc.modules.database.entities.AccountEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateAccountRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String password;

    @NotBlank
    private String fname;

    @NotBlank
    private String lname;

    private String avt;

    public AccountEntity toEntity(Integer accountId) {
        return new AccountEntity(accountId, email, phone, password, fname, lname, 0, avt);
    }
}
