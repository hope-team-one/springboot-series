package com.imooc.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

//将这个切片类成为spring容器的一部分
@Aspect
@Component
//切片类
public class TimeAspect {
    //拦截usercontroller类中的所有方法
    @Around("execution(* com.imooc.controller.UserController.*(..))")
    public Object handlerControllerMethod(ProceedingJoinPoint point) throws Throwable {
        System.out.println("time aspect start");

        Object [] args = point.getArgs();
        for (Object arg : args) {
            System.out.println("arg is " + arg);
        }
        //调用的方法返回的是什么这里返回的就是什么
        Object object = point.proceed();

        System.out.println("time aspect end");
        return object;
    }
}
