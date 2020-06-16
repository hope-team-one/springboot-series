package com.imooc.core.validatecode.sms;

import com.imooc.core.properties.SecurityProperties;
import com.imooc.core.validatecode.ValidateCode;
import com.imooc.core.validatecode.ValidateCodeGenerator;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 内部默认验证码生成器，有些用户会自定义，但是有的用户并不会自定义，所以我们需要提供一个默认的验证码生成器
 * 定义个bean叫smsCodeGenerator 注入的时候对象名写这个就会注入这个实现类
 */
@Component("smsCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {
    private static org.apache.log4j.Logger log = Logger.getLogger(SmsCodeGenerator.class);

    @Autowired
    private SecurityProperties securityProperties;

    /**
     * 优先级请求->用户配置->默认配置
     * 有请求则用请求里面的无请求则用用户配置的，都无则用默认的
     * @param request
     * @return
     */
    @Override
    public ValidateCode createImageCode(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
        System.out.println("短信验证码："+code);
        //最基础的验证码类 没有图片 只有code和过期时间
        return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
    }

    public SecurityProperties getSecurityProperties() {
        return securityProperties;
    }

    public void setSecurityProperties(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public SmsCodeGenerator(){

    }
    public SmsCodeGenerator(SecurityProperties securityProperties){
        this.securityProperties = securityProperties;
    }
}
