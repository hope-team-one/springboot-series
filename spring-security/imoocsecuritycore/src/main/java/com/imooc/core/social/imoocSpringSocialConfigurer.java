package com.imooc.core.social;

import org.springframework.social.security.SocialAuthenticationFilter;
import org.springframework.social.security.SpringSocialConfigurer;

public class imoocSpringSocialConfigurer extends SpringSocialConfigurer {
    private String processesUrl;
    private SocialAuthenticationFilterProcessor socialAuthenticationFilterProcessor;
    public imoocSpringSocialConfigurer(String processesUrl){
        this.processesUrl = processesUrl;
    }
    /**
     * 覆盖掉social原来的postprocess 自己定义 改变social默认拦截的请求前缀
     * @param object 要放到过滤器链上的filter
     * @param <T>
     * @return
     */
    @SuppressWarnings("unchecked")
    @Override
    protected <T> T postProcess(T object) {
        SocialAuthenticationFilter filter = (SocialAuthenticationFilter) super.postProcess(object);
        filter.setFilterProcessesUrl(processesUrl);
        //只有app实现了这个接口 当是浏览器的时候 会为空 就不需要指定
        if(socialAuthenticationFilterProcessor != null){
            socialAuthenticationFilterProcessor.process(filter);
        }
        return (T)filter;
    }

    public SocialAuthenticationFilterProcessor getSocialAuthenticationFilterProcessor() {
        return socialAuthenticationFilterProcessor;
    }

    public void setSocialAuthenticationFilterProcessor(SocialAuthenticationFilterProcessor socialAuthenticationFilterProcessor) {
        this.socialAuthenticationFilterProcessor = socialAuthenticationFilterProcessor;
    }
}
