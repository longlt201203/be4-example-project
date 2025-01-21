package com.be4.qltc.utils;

import org.springframework.security.core.GrantedAuthority;

public class QltcAuthority implements GrantedAuthority {
    public static QltcAuthority of(Integer role) {
        return new QltcAuthority(role.toString());
    }

    private String authority;

    @Override
    public String getAuthority() {
        return authority;
    }

    private QltcAuthority(String authority) {
        this.authority = authority;
    }
}
