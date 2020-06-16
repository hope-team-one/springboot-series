package com.imooc.core.social;

import org.apache.commons.collections.CollectionUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 展示当前存在的providerId是否绑定了我们的用户
 */
@Component("connect/status")
public class imoocConnectionStatusView extends AbstractView {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void renderMergedOutputModel(Map<String, Object> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        Map<String, List<Connection<?>>> connections = (Map<String, List<Connection<?>>>)map.get("connectionMap");
        Map<String,Boolean> result = new HashMap<>();
        for(String key : connections.keySet()){
            result.put(key, CollectionUtils.isNotEmpty(connections.get(key)));
        }
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(result));
    }
}
