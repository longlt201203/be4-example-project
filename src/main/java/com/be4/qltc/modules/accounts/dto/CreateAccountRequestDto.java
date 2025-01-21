package com.be4.qltc.modules.accounts.dto;

import com.be4.qltc.modules.database.entities.AccountEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Data
public class CreateAccountRequestDto {
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

    public AccountEntity toEntity() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return new AccountEntity(email, phone, encoder.encode(password), fname, lname, 0, avt);
    }
}
