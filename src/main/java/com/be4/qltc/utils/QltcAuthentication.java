package com.be4.qltc.utils;

import com.be4.qltc.modules.database.entities.AccountEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

public class QltcAuthentication implements Authentication {
    private AccountEntity account;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account != null ? List.of(QltcAuthority.of(account.getRole())) : List.of();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return account;
    }

    @Override
    public boolean isAuthenticated() {
        return account != null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException();
    }

    @Override
    public String getName() {
        return AccountEntity.class.getName();
    }

    public QltcAuthentication(AccountEntity account) {
        this.account = account;
    }
}
