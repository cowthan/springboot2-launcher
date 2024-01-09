package com.ddy.dyy.web.uc;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@Slf4j
@MapperScan({"com.ddy.dyy.web.uc.mapper"})
@EnableConfigurationProperties
@ComponentScan
public class UserCenterAutoConfiguration {

}
