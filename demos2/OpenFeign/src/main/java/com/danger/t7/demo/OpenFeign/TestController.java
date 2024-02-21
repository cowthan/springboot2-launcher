package com.danger.t7.demo.OpenFeign;

import com.ddy.dyy.web.lang.Lang;
import com.ddy.dyy.web.models.LogicException;
import com.ddy.dyy.web.models.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * .
 */
@RestController
@RequestMapping("/api")
public class TestController {


    @GetMapping("/get")
    public Response<String> test1() {
        return Response.ok("OpenFeign");
    }


    @GetMapping("/get2")
    public Response<Object> get2() {
        return Response.error(406, "error 406");
    }

    @GetMapping("/get3")
    public Response<Object> get3() {
        throw new LogicException(405, "error 405");
    }

    @GetMapping("/get500")
    public Response<Object> get500() {
        throw new RuntimeException("bad thing happened");
    }

    @GetMapping("/getTimeout")
    public Response<Object> getTimeout() {
        Lang.sleep(100000);
        return Response.ok("");
    }


}
