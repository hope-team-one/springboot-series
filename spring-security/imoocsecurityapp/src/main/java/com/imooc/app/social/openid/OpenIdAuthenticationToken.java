package com.imooc.app.social.openid;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class OpenIdAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = -1451451515124154L;

    public OpenIdAuthenticationToken(String openId,String providerId) {
        super(null);
        this.principal = openId;
        this.providerId = providerId;
        setAuthenticated(false);
    }
    public OpenIdAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }
    private final Object principal;//openid
    private String providerId;
    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
    public String getProviderId(){
        return providerId;
    }
}
