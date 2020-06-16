package com.imooc.core.validatecode.image;

import com.imooc.core.repository.ValidateCodeRepository;
import com.imooc.core.validatecode.ValidateCode;
import com.imooc.core.validatecode.ValidateCodeException;
import com.imooc.core.validatecode.ValidateCodeProcessor;
import com.imooc.core.validatecode.ValidateCodeType;
import com.imooc.core.validatecode.impl.AbstractValidateCodeProcessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
@Component
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> {
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    @Autowired
    private ValidateCodeRepository validateCodeRepository;
    @Override
    protected void send(ServletWebRequest request, ImageCode validateCode) throws Exception {
        ImageIO.write(validateCode.getImage(), "JPEG", request.getResponse().getOutputStream());
    }

    @Override
    public void validate(ServletWebRequest request) throws ServletRequestBindingException {
        ValidateCodeType validateCodeType = getValidateCodeType(request);
        //获取session中存入的验证码
        ValidateCode codeInSession = validateCodeRepository.get(request,validateCodeType);
        //获取到表单post请求中的imageCode参数的值
        String codeInRequest = null;
        codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("验证码不能为空");
        }
        if (codeInSession == null) {
            throw new ValidateCodeException("验证码不存在");
        }
        if (codeInSession.isExpried()) {
            validateCodeRepository.remove(request,validateCodeType);
//            sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX+"image");
            throw new ValidateCodeException("验证码过期");
        }
        if (!codeInSession.getCode().equals(codeInRequest)) {
            throw new ValidateCodeException("验证码不匹配");
        }
        validateCodeRepository.remove(request,validateCodeType);
//        sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX+"image");
    }
}
