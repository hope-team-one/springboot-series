package com.imooc.app.repository;

import com.imooc.core.repository.ValidateCodeRepository;
import com.imooc.core.validatecode.ValidateCode;
import com.imooc.core.validatecode.ValidateCodeException;
import com.imooc.core.validatecode.ValidateCodeType;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Override
    public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType type) {
        redisTemplate.opsForValue().set(buildKey(request,type),code,30, TimeUnit.MINUTES);
    }
    private String buildKey(ServletWebRequest request,ValidateCodeType validateCodeType){
        String deviceId = request.getHeader("deviceId");
        if(StringUtils.isBlank(deviceId)){
            throw new ValidateCodeException("请在请求头中携带deviceId参数");
        }
        return "code:"+validateCodeType.toString().toLowerCase()+":"+deviceId;
    }
    @Override
    public ValidateCode get(ServletWebRequest request, ValidateCodeType type) {
        Object object = redisTemplate.opsForValue().get(buildKey(request,type));
        if(object==null){
            return null;
        }
        return (ValidateCode)object;
    }

    @Override
    public void remove(ServletWebRequest request, ValidateCodeType type) {
        redisTemplate.delete(buildKey(request,type));
    }

}
