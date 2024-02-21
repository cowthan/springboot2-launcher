package com.example.demo.test.SpringAsync;

import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncBizService {
    /*
    1 需要@EnableAsync
    2 @Async 的返回值只能为 void 或者 Future，若异步方法需要返回值，使用Future<String>、new AsyncResult<>()
    3 默认的线程池 SimpleAsyncTaskExecutor，可通过实现 AsyncConfigurer 接口替换默认线程池，只能定义一个， @Async不带参数则使用默认线程池
    4 业务线程池，通过普通的ThreadPoolExecutor的Bean定义实现，通过 @Async("myThreadPool")参数指定
     */
    //@Async
    @Async("myThreadPool")
    public void testAsync(){
        try {
            //暂停一会线程
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            log.error("", e);
            Thread.currentThread().interrupt();
        }
        log.info(Thread.currentThread().getName());
    }
}
