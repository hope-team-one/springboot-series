package com.imooc.controller;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.ResponseEntity;
import org.springframework.social.connect.*;
import org.springframework.social.connect.web.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 自定义MyConntectController，不直接跳转视图，而是返回json数据供页面展示，直接跳转视图不适合实际，所以需要返回json数据
 * 仿造ConnectController类来就可以了
 */
@RestController
@RequestMapping("/alibaba")
public class MyConnectController implements InitializingBean {
    private static final Log logger = LogFactory.getLog(MyConnectController.class);
    private final ConnectionFactoryLocator connectionFactoryLocator;
    private final ConnectionRepository connectionRepository;
    private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

    @Inject
    public MyConnectController(ConnectionFactoryLocator connectionFactoryLocator, ConnectionRepository connectionRepository) {
        this.connectionFactoryLocator = connectionFactoryLocator;
        this.connectionRepository = connectionRepository;
    }
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public ResponseEntity<Map<String, Boolean>> connectionStatus(NativeWebRequest request, Model model) {
        this.setNoCache(request);
        this.processFlash(request, model);
        Map<String, List<Connection<?>>> connections = this.connectionRepository.findAllConnections();
        model.addAttribute("providerIds", this.connectionFactoryLocator.registeredProviderIds());
        model.addAttribute("connectionMap", connections);
        Map<String, Boolean> map = new HashMap<>();
        for (String providerId : connections.keySet()){
            map.put(providerId, CollectionUtils.isNotEmpty(connections.get(providerId)));
        }
        return ResponseEntity.ok(map);
    }


    private void setNoCache(NativeWebRequest request) {
        HttpServletResponse response = (HttpServletResponse)request.getNativeResponse(HttpServletResponse.class);
        if (response != null) {
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 1L);
            response.setHeader("Cache-Control", "no-cache");
            response.addHeader("Cache-Control", "no-store");
        }

    }
    private void processFlash(WebRequest request, Model model) {
        this.convertSessionAttributeToModelAttribute("social_addConnection_duplicate", request, model);
        this.convertSessionAttributeToModelAttribute("social_provider_error", request, model);
        model.addAttribute("social_authorization_error", this.sessionStrategy.getAttribute(request, "social_authorization_error"));
        this.sessionStrategy.removeAttribute(request, "social_authorization_error");
    }
    private void convertSessionAttributeToModelAttribute(String attributeName, WebRequest request, Model model) {
        if (this.sessionStrategy.getAttribute(request, attributeName) != null) {
            model.addAttribute(attributeName, Boolean.TRUE);
            this.sessionStrategy.removeAttribute(request, attributeName);
        }

    }
    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
