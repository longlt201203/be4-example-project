package com.be4.qltc.modules.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.be4.qltc.modules.auth.dto.BasicLoginDto;
import com.be4.qltc.modules.database.entities.AccountEntity;
import com.be4.qltc.modules.database.repositories.AccountRepository;
import com.be4.qltc.utils.configs.QltcConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private QltcConfiguration configuration;

    public String basicLogin(BasicLoginDto dto) {
        Optional<AccountEntity> result = accountRepository.findByEmail(dto.getEmail());
        if (result.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password!");
        }
        AccountEntity account = result.get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(dto.getPassword(), account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password!");
        }
        Instant now = Instant.now();
        Algorithm algorithm = Algorithm.HMAC256(configuration.getJwtSecret());
        return JWT.create()
                .withSubject(account.getAccountId().toString())
                .withIssuer(configuration.getJwtIssuer())
                .withIssuedAt(now)
                .withExpiresAt(now.plus(30, ChronoUnit.DAYS))
                .sign(algorithm);
    }

    public Optional<AccountEntity> verifyAccessToken(String accessToken) {
        Algorithm algorithm = Algorithm.HMAC256(configuration.getJwtSecret());
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer(configuration.getJwtIssuer())
                .build()
                .verify(accessToken);
        Integer accountId = Integer.valueOf(decodedJWT.getSubject());
        Optional<AccountEntity> result = accountRepository.findById(accountId);
        return result;
    }

    public AccountEntity getProfile() {
        SecurityContext context = SecurityContextHolder.getContext();
        return (AccountEntity) context.getAuthentication().getPrincipal();
    }
}
