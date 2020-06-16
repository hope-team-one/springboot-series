package com.imooc.core.validatecode;

import com.imooc.core.properties.SecurityProperties;
import com.imooc.core.validatecode.image.ImageCodeGenerator;
import com.imooc.core.validatecode.image.ImageCodeProcessor;
import com.imooc.core.validatecode.sms.SmsCodeGenerator;
import com.imooc.core.validatecode.sms.SmsCodeProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidateCodeBeanConfig {
    @Autowired
    private SecurityProperties securityProperties;
    //将validatecodegeneraotr接口做成可配置的
    @Bean
    //如果spring容器中已经存在了这个name的bean，那么就不会创建下面的了,直接使用这个bean去注入，这样就可以让用户自定义
    @ConditionalOnMissingBean(name = "imageCodeGenerator")
    public ValidateCodeGenerator imageCodeGenerator(){
        //将默认的验证码生成器提供给用户 当用户注入ValidateCodeGenerator这个接口的时候
        ImageCodeGenerator imageCodeGenerator = new ImageCodeGenerator();
        imageCodeGenerator.setSecurityProperties(securityProperties);
        return imageCodeGenerator;
    }
    @Bean
    @ConditionalOnMissingBean(SmsCodeGenerator.class)
    public ValidateCodeGenerator smsCodeGenerator(){
        return new SmsCodeGenerator(securityProperties);
    }

    @Bean
    //如果spring容器中已经存在了这个name的bean，那么就不会创建下面的了,直接使用这个bean去注入，这样就可以让用户自定义
    @ConditionalOnMissingBean(SmsCodeProcessor.class)
    public ValidateCodeProcessor smsCodeProcessor(){
        return new SmsCodeProcessor();
    }

    @Bean
    @ConditionalOnMissingBean(ImageCodeProcessor.class)
    public ValidateCodeProcessor imageCodeProcessor(){
        return new ImageCodeProcessor();
    }

}
