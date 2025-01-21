package com.be4.qltc.modules.accounts.dto;

import com.be4.qltc.modules.database.entities.AccountEntity;
import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class AccountResponseDto {
    private Integer accountId;

    private String email;

    private String phone;

    private String fname;

    private String lname;

    private Integer role;

    private String avt;

    private Instant createdAt;

    private Instant updatedAt;

    public AccountResponseDto(Integer accountId, String email, String phone, String fname, String lname, Integer role, String avt, Instant createdAt, Instant updatedAt) {
        this.accountId = accountId;
        this.email = email;
        this.phone = phone;
        this.fname = fname;
        this.lname = lname;
        this.role = role;
        this.avt = avt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AccountResponseDto fromEntity(AccountEntity entity) {
        return new AccountResponseDto(
                entity.getAccountId(),
                entity.getEmail(),
                entity.getPhone(),
                entity.getFname(),
                entity.getLname(),
                entity.getRole(),
                entity.getAvt(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public static List<AccountResponseDto> fromEntities(List<AccountEntity> entities) {
        return entities.stream().map(AccountResponseDto::fromEntity).toList();
    }
}
