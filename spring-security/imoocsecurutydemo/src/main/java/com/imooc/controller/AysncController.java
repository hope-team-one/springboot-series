package com.imooc.controller;

import com.imooc.dto.MockQueue;
import com.imooc.web.async.DeferredResultHolder;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.Callable;


@RestController
public class AysncController {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private MockQueue mockQueue;
    @Autowired
    private DeferredResultHolder deferredResultHolder;
    @RequestMapping("/order")
    public DeferredResult<String> order() throws InterruptedException {
        logger.info("主线程开始");
        String ordernum = RandomStringUtils.randomNumeric(8);
        mockQueue.setPlaceOrder(ordernum);
        DeferredResult<String> result = new DeferredResult();
        deferredResultHolder.getMap().put(ordernum,result);
        logger.info("主线程结束");
        return result;
    }
}
