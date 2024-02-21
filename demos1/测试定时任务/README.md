
一个比较纯粹的起步SpringBoot工程

默认需要连一个库，并需要先建表：db.sql


spring有一个 QuartzAutoConfiguration  QuartzProperties，依赖 spring-context-support和spring-tx
- job数据存储：默认走RAMJobStore，可配置： prop.put("org.quartz.jobStore.class", "org.springframework.scheduling.quartz.LocalDataSourceJobStore");