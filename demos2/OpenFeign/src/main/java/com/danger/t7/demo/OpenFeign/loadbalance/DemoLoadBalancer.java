package com.danger.t7.demo.OpenFeign.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.DefaultResponse;
import org.springframework.cloud.client.loadbalancer.Request;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.loadbalancer.core.ReactorServiceInstanceLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Mono;

/**
 * Load balancer for spring cloud delegated with rpc load balancer
 */
@Slf4j
public class DemoLoadBalancer implements ReactorServiceInstanceLoadBalancer {
    private ServiceInstanceListSupplier serviceInstanceListSupplier;
    private final AtomicInteger position = new AtomicInteger(0);

    public DemoLoadBalancer(ServiceInstanceListSupplier serviceInstanceListSupplier) {
        this.serviceInstanceListSupplier = serviceInstanceListSupplier;
    }

    @Override
    public Mono<Response<ServiceInstance>> choose(Request request) {
        return choose();
    }

    @Override
    public Mono<Response<ServiceInstance>> choose() {
        List<ServiceInstance> instances = serviceInstanceListSupplier.get().blockFirst();
        int pos = Math.abs(this.position.incrementAndGet());
        return null == instances ? Mono.empty() : Mono.just(new DefaultResponse(instances.get(pos % instances.size())));
    }

}
