package com.imooc.core.validatecode.impl;

import com.imooc.core.repository.ValidateCodeRepository;
import com.imooc.core.validatecode.*;
import com.imooc.core.validatecode.image.ImageCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * 此抽象类实现了validateCodeProcessor的create，另外自己再提供一个send抽象方法，给真正做事的processor类实现
 * @param <C>
 */
@Component
public abstract class AbstractValidateCodeProcessor<C> implements ValidateCodeProcessor {
    /**
     * 依赖搜索 系统启动的时候，收集系统中ValidateCodeGenerator的实现类首字母小写
     * @param request
     * @throws Exception
     */
    @Autowired
    protected Map<String,ValidateCodeGenerator> validateCodeGeneratorMap;
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();
    @Autowired
    private ValidateCodeRepository validateCodeRepository;

    /**
     * 这个抽象接口实现了create接口  里面所有的方法都是在create里面进行了调用 等于就是说单纯的实现了create这个方法
     * 然后编写了一个抽象的send方法 图片验证码和短信验证码都继承这个抽象类 并且需要实现他的send方法
     * 哪个AbstractValidateCodePoecessor的子类调用的create方法  那么send就会进入到对应的那个子类
     * @param request
     * @throws Exception
     */
    @Override
    public void create(ServletWebRequest request) throws Exception {
        C validateCode =generate(request);
        save(request,validateCode);
        send(request,validateCode);
    }

    /**
     * 给sms和image实现，由他们各自去决定怎么发送这个验证码 哪个对象调用的父类的create方法，就执行哪个对象的send方法
     * @param request
     * @param validateCode
     * @throws Exception
     */
    protected abstract void send(ServletWebRequest request, C validateCode)throws Exception;
    /**
     * 生成校验码
     */
    @SuppressWarnings("unchecked")
    private C generate(ServletWebRequest request){
        String type = getProcessorType(request);
        ValidateCodeGenerator validateCodeGenerator = validateCodeGeneratorMap.get(type+"CodeGenerator");
        return (C) validateCodeGenerator.createImageCode(request);
    }
    /**
     * 根据请求的url获取校验码的类型 根据请求url的后半段来判断你是image还是sms
     */
    private String getProcessorType(ServletWebRequest request){
        return StringUtils.substringAfter(request.getRequest().getRequestURI(),"/code/");
    }
    /**
     * 保存校验码
     */
    private void save(ServletWebRequest request,C validateCode){
        ValidateCode validateCode1 = (ValidateCode)validateCode;
        ValidateCode validateCode2 = new ValidateCode(validateCode1.getCode(),((ValidateCode) validateCode).getExpireTime());
        validateCodeRepository.save(request,validateCode2,getValidateCodeType(request));
//        sessionStrategy.setAttribute(request,SESSION_KEY_PREFIX+getProcessorType(request),validateCode2);
    }

    public ValidateCodeType getValidateCodeType(ServletWebRequest request){
        String type = StringUtils.substringBefore(getClass().getSimpleName(),"CodeProcessor");
        if(type.equals("Sms")){
            return ValidateCodeType.SMS_CODE_TYPE;
        }else if(type.equals("Image")){
            return ValidateCodeType.IMAGE_CODE_TYPE;
        }
        return null;
    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public void validate(ServletWebRequest request) throws ServletRequestBindingException {
//        ValidateCodeType validateCodeType = getValidateCodeType(request);
//        C codeInSession = (C)validateCodeRepository.get(request,validateCodeType);
//        String codeInRequest;
//        try {
//            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),validateCodeType.getType());
//        }catch (ServletRequestBindingException e){
//            throw new ValidateCodeException("获取验证码值失败");
//        }
//        if (StringUtils.isBlank(codeInRequest)) {
//            throw new ValidateCodeException("验证码不能为空");
//        }
//        if (codeInSession == null) {
//            throw new ValidateCodeException("验证码不存在");
//        }
//        if (codeInSession.isExpried()) {
//            sessionStrategy.removeAttribute(request, ValidateCodeProcessor.SESSION_KEY_PREFIX+"sms");
//            throw new ValidateCodeException("验证码过期");
//        }
//        if (!codeInSession.getCode().equals(codeInRequest)) {
//            throw new ValidateCodeException("验证码不匹配");
//        }
//    }
}
