package com.danger.t7.demo.OpenFeign.demo.api;

import com.ddy.dyy.web.models.Response;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * GitHub
 */
public interface MyApi {

//    @GetMapping("/get")
    @GetMapping("/api/get")
//    @RequestLine("GET /api/get")
    Response<String> test1();

}
