package com.imooc.core.authtication.mobile;

import com.imooc.core.service.CommonUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class SmsCodeAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private CommonUserDetailsService commonUserDetailsService;
    /**
     *
     * @param authentication 这就是传入的smstoken 里面存放了mobile手机号
     * @return
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken =(SmsCodeAuthenticationToken)authentication;
        //获得smstoken在filter里面存入的手机号 根据手机号去数据库查用户信息
        UserDetails userDetails = commonUserDetailsService.loadByPhone(String.valueOf(authenticationToken.getPrincipal()));
        if(userDetails==null){
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }
        //不为空表示查到了用户信息  将用户信息和用户权限传入 这个权限是在userdetalsservice里面查出用户信息之后来封装的
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails,userDetails.getAuthorities());
        //之前未认证的token里面的details 拷贝到已认证的token的details里面
        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    /**
     * 根据这个方法来判断 我当前token需要用哪个provider来处理
     * 传入一个token 然后你判断这个token是不是这个provider所需要的token 如果是就返回true
     * @return
     */
    @Override
    public boolean supports(Class<?> authenticate) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authenticate);
    }

    public void setUserDetailsService(CommonUserDetailsService userDetailsService) {
        this.commonUserDetailsService = userDetailsService;
    }
}
