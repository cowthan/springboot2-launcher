package com.gee.blg.zk;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.client.ZKClientConfig;

/**
 * CuratorMain
 */
@Slf4j
public class CuratorMain {

            private static final String ZK_ADDRESS = "172.16.101.80:2181,172.16.101.81:2181,172.16.101.82:2181";
//    private static final String ZK_ADDRESS = "172.16.101.225:2181,172.16.101.226:2181,172.16.101.227:2181";
//    private static final String ZK_ADDRESS = "172.16.101.116:2181";
//    private static final String ZK_ADDRESS = "172.16.101.142:2181";
//    private static final String ZK_ADDRESS = "localhost:2181";

    public static void main(String[] args) throws Exception {



//        Thread.sleep(50000);
        log.info(">>>>>>>>>>>>>>>> before new");

//        ZKClientConfig conf = new ZKClientConfig();
//        conf.setProperty(ZKClientConfig.ENABLE_CLIENT_SASL_KEY, "false");

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder();
        CuratorFramework curatorFramework =
                builder
                        .connectString(ZK_ADDRESS)
                        .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                        .connectionTimeoutMs(20000)
                        .sessionTimeoutMs(50000)
//                        .zkClientConfig(conf)
                        .build();
        log.info(">>>>>>>>>>>>>>>> before start");
        curatorFramework.start(); /// 【有固定30秒开销，即使是连localhost也是】】
        log.info(">>>>>>>>>>>>>>>> 阻塞等待connected");
        curatorFramework.blockUntilConnected();// 如果不是连localhost，也会有30秒开销】
        log.info(">>>>>>>>>>>>>>>> zookeeper starter success");
        String data = new String(curatorFramework.getData().forPath("/"));
        log.info(">>>>>>>>>>>>>>>> 输出结果：" + data);
    }
}