package com.example.demo.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.example.demo.BaseController;
import com.example.demo.models.ListRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TestController extends BaseController {

    @GetMapping("/page")
    public ListRequest listRequests(ListRequest request) {
        request.setTime3(new Date());
        request.setTime4(System.currentTimeMillis());
        return request;
    }

    @Getter
    @Setter
    public static class Body1 {
        private String param1;
    }


    @Getter
    @Setter
    public static class Body2 {
        private String param2;
    }

    @PostMapping("testPost")
    public Object testPost(@RequestBody Body1 b1) {
        Map ret = new HashMap();
        ret.put("b1", b1);
//        ret.put("b2", b2);
        return ret;
    }
}