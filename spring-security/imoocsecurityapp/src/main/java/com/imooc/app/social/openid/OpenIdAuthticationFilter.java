package com.imooc.app.social.openid;

import com.imooc.core.authtication.SecurityConstants;
import com.imooc.core.authtication.mobile.SmsCodeAuthenticationToken;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OpenIdAuthticationFilter extends AbstractAuthenticationProcessingFilter {
    private String openIdParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_OPENID;
    private String providerIdParameter = SecurityConstants.DEFAULT_PARAMETER_NAME_PROVIDERID;
    private boolean postOnly = true;

    public OpenIdAuthticationFilter(){
        super(new AntPathRequestMatcher(SecurityConstants.DEFAULT_LOGIN_PROCESSING_URL_OPENID,"POST"));
    }
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(postOnly && !request.getMethod().equals("POST")){
            throw new AuthenticationServiceException("Authenticaction method not supported:"+request.getMethod());
        }

        String openid = obtainOpenId(request);
        String providerId = obtainProviderId(request);

        if(openid==null){
            openid = "";
        }
        if(providerId==null){
            providerId = "";
        }
        openid = openid.trim();
        providerId = providerId.trim();
        OpenIdAuthenticationToken authRequest = new OpenIdAuthenticationToken(openid,providerId);
        setDetails(request,authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    protected String obtainOpenId(HttpServletRequest request){
        return request.getParameter(this.openIdParameter);
    }

    protected String obtainProviderId(HttpServletRequest request){
        return request.getParameter(this.providerIdParameter);
    }

    //将请求构建成一个details 放入token
    protected void setDetails(HttpServletRequest request, OpenIdAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }
}
