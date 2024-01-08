
springboot2快速启动模版，包含：
- 常用工具类，Lang、Md5、JsonUtils
- http工具类，基于okhttp的HttpEmitter
- web常用models，如Response，LogicException
- web常用组件：请求日志-DyLoggingInterceptor、BaseController、接口全局异常-DyWebExceptionHandler、接口返回统一规格-DyRestResponseWrapAdvice
- logback配置：logback-spring.xml

```
git clone -b demo https://github.com/cowthan/springboot2-launcher.git
```

如果要修改libs逻辑，需要切换到分支-master