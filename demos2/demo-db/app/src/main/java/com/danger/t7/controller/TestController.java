package com.danger.t7.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @RequestMapping("test1")
    public String test1(){
        return "123";
    }
}