package com.imooc.core.validatecode.sms;

import com.imooc.core.properties.SecurityProperties;
import com.imooc.core.repository.ValidateCodeRepository;
import com.imooc.core.validatecode.ValidateCode;
import com.imooc.core.validatecode.ValidateCodeException;
import com.imooc.core.validatecode.ValidateCodeProcessor;
import com.imooc.core.validatecode.ValidateCodeType;
import com.imooc.core.validatecode.image.ImageCode;
import com.imooc.core.validatecode.impl.AbstractValidateCodeProcessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
@Component
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    @Autowired
    private ValidateCodeRepository validateCodeRepository;
    @Override
    protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
        System.out.println("向手机发送短信验证码");
    }

    @Override
    public void validate(ServletWebRequest request) throws ServletRequestBindingException {
        ValidateCodeType validateCodeType = getValidateCodeType(request);
        //获取redis中的验证码
        ValidateCode codeInSession = (ValidateCode)validateCodeRepository.get(request,validateCodeType);
        //获取到表单post请求中的smsCode参数的值
        String codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "smsCode");
        if (StringUtils.isBlank(codeInRequest)) {//异常在这里跑出来能够被自定义异常处理器获取 在validateCodeRepository也行
            throw new ValidateCodeException("验证码不能为空");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpried()) {
            validateCodeRepository.remove(request,validateCodeType);
//            sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX+"sms");
            throw new ValidateCodeException("验证码过期");
        }
        if (!codeInSession.getCode().equals(codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        validateCodeRepository.remove(request,validateCodeType);
//        sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX+"sms");
    }

}
