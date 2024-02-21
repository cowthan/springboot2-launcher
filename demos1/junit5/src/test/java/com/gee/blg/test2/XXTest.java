package com.gee.blg.test2;

import com.gee.blg.test.SpringBootJUnitApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

/**
 * 测试类和启动类不在一个包下，则需要手动引入：@SpringBootTest(classes = SpringBootJUnitApplication.class)
 */
@SpringBootTest(classes = SpringBootJUnitApplication.class)
public class XXTest {
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void test4() throws Exception {
        System.out.println("Outer test run ......");
    }
}