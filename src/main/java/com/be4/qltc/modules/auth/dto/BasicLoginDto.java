package com.be4.qltc.modules.auth.dto;

import lombok.Data;

@Data
public class BasicLoginDto {
    private String email;
    private String password;
}
