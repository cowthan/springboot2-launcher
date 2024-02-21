package com.danger.t7.demo.OpenFeign.loadbalance;

import java.util.Arrays;
import java.util.List;

import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import reactor.core.publisher.Flux;

/**
 * service instance list supplier with call back triggered by list changes
 */
public class DemoServiceInstanceListSupplier implements ServiceInstanceListSupplier {

    private final String serviceId;


    public DemoServiceInstanceListSupplier(String serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return Flux.just(Arrays.asList(
                new DefaultServiceInstance(getServiceId() + "1", getServiceId(), "localhost", 8090, false),
                new DefaultServiceInstance(getServiceId() + "2", getServiceId(), "localhost", 9092, false),
                new DefaultServiceInstance(getServiceId() + "3", getServiceId(), "localhost", 9999, false)));
    }

}
