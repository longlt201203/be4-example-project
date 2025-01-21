package com.be4.qltc.utils.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@Import(QltcSecurityConfiguration.class)
@Data
public class QltcConfiguration {
    @Value("${qltc.jwt.secret}")
    private String jwtSecret;

    @Value("${qltc.jwt.issuer}")
    private String jwtIssuer;
}
