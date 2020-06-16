package com.imooc.core.service.impl;

import com.imooc.core.commonbean.UserInfo;
import com.imooc.core.repository.UserInfoRepository;
import com.imooc.core.service.CommonUserDetailsService;
import com.imooc.core.validatecode.ValidateCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.social.security.SocialUser;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MyUserDetailsService implements CommonUserDetailsService, SocialUserDetailsService {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private PasswordEncoder passwordEncoder;
    //实际项目中这里注入mybatis的mapper等，可以通过注入的mapper去访问到数据库
    @Autowired
    private UserInfoRepository userInfoRepository;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        logger.info("用户名："+s);
        //通过用户名查找用户信息 传入的就是用户名
        //没有根据数据库的数据，传入实际权限
        //实现通过用户输入的用户名 在数据库查出用户名密码，然后让security根据查出的用户来和页面输入的用户进行匹配
        //根据找到的用户信息判断用户是否被冻结
        String password  = passwordEncoder.encode("123456");
        if(true) {
            throw new ValidateCodeException("无法获取用户信息");
        }
        logger.info("加密后的密码："+password);
        return new User(s,password,true,true,true,true,AuthorityUtils.commaSeparatedStringToAuthorityList("admin,ROLE_USER"));
    }

    @Override
    public UserDetails loadByPhone(String phone) {
        UserInfo userInfo = userInfoRepository.getUserInfoByPhone(phone);
        if (userInfo==null) {
            throw new InternalAuthenticationServiceException("无法通过手机号获取用户信息");
        }
        //模拟数据库中存放的用户的角色权限 实际应用中 这个roles的值应该从数据库中读取 然后再循环放入到grantedAuthorities中
        List<String> roles = new ArrayList<>();
        roles.add("ROLE_EMPLOYEE");
//        userInfo.setRoles(roles);
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (String role : roles) {
            //封装用户信息和角色信息到SecurityContextHolder全局缓存中
            grantedAuthorities.add(new SimpleGrantedAuthority(role));
        }
        System.out.println("用户名："+userInfo.getName()+"，密码："+userInfo.getPassword()+"，手机号："+userInfo.getPhone()+"，地址："+userInfo.getAddress());
        return new User(userInfo.getName(),userInfo.getPassword() ,grantedAuthorities);
    }

    @Override
    public SocialUserDetails loadUserByUserId(String s) throws UsernameNotFoundException {
        logger.info("社区登陆用户id："+s);
        //通过用户名查找用户信息 传入的就是用户名
        //没有根据数据库的数据，传入实际权限
        //实现通过用户输入的用户名 在数据库查出用户名密码，然后让security根据查出的用户来和页面输入的用户进行匹配
        //根据找到的用户信息判断用户是否被冻结
        String password  = passwordEncoder.encode("123456");
        logger.info("加密后的密码："+password);
        return new SocialUser(
                s,password,
                true,
                true,
                true,
                true,
                AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));//ROLE_ADMIN
    }
}
