package demo.mvc.controller;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test")
    public Object testRequest(String name) throws JsonProcessingException {
        System.out.println("执行应用控制器方法，参数name为" + name);
        //重定向到指定页面
        Map ret = new HashMap();
        ret.put("code", "0");
        ret.put("msg", "xxxxxxx");
        return new ObjectMapper().writeValueAsString(ret);
    }
}