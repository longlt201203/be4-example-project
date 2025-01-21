package com.be4.qltc.modules.database.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.Instant;

@Entity
@Data
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer accountId;

    @Column(length = 60, unique = true, nullable = false)
    private String email;

    @Column(length = 12, unique = true, nullable = false)
    private String phone;

    @Column(length = 30, nullable = false)
    private String password;

    @Column(length = 30, nullable = false)
    private String fname;

    @Column(length = 30, nullable = false)
    private String lname;

    @Column(nullable = false)
    private Integer role;

    @Column(length = 600)
    private String avt;

    @Column
    @CreatedDate
    private Instant createdAt;

    @Column
    @LastModifiedDate
    private Instant updatedAt;

    public AccountEntity() {
    }

    public AccountEntity(String email, String phone, String password, String fname, String lname, Integer role, String avt) {
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.role = role;
        this.avt = avt;
    }

    public AccountEntity(Integer accountId, String email, String phone, String password, String fname, String lname, Integer role, String avt) {
        this.accountId = accountId;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.fname = fname;
        this.lname = lname;
        this.role = role;
        this.avt = avt;
    }
}
