package com.imooc.web.async;

import com.imooc.dto.MockQueue;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private MockQueue mockQueue;

    @Autowired
    private DeferredResultHolder deferredResultHolder;

    private Logger logger = LoggerFactory.getLogger(getClass());

    //当系统启动的时候进行监听
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        new Thread(() -> {
            while(true){
                if(StringUtils.isNotBlank(mockQueue.getCompleteOrder())){
                    String ordernum = mockQueue.getCompleteOrder();
                    logger.info("返回订单处理结果："+ordernum);
                    deferredResultHolder.getMap().get(ordernum).setResult("place order success");
                    mockQueue.setCompleteOrder(null);
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }
}