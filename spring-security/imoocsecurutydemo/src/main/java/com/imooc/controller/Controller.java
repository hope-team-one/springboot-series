package com.imooc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/social1")
@RestController
public class Controller {
    @RequestMapping(value = "/social1",method = RequestMethod.GET)
    public void so(){
        System.out.println("social1");
    }
}
