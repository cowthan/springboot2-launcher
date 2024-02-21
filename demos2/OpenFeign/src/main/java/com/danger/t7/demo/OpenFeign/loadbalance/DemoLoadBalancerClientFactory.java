package com.danger.t7.demo.OpenFeign.loadbalance;

import java.util.Collections;

import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;

/**
 * factory for loadbalancer client
 */
public class DemoLoadBalancerClientFactory extends LoadBalancerClientFactory {

    public DemoLoadBalancerClientFactory() {
        registerDefaultConfiguration();
    }

    protected void registerDefaultConfiguration() {
//        LoadBalancerClientSpecification loadBalancerSpecification = new LoadBalancerClientSpecification(
//                "default.loadbalancer.configuration", new Class[]{SpringCloudLoadBalancerConfiguration.class}
//        );
//        this.setConfigurations(Collections.singletonList(loadBalancerSpecification));
    }

    @Override
    public ReactiveLoadBalancer<ServiceInstance> getInstance(String serviceId) {
        return getInstance(serviceId, DemoLoadBalancer.class);
    }

}
