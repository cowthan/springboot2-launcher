[TOC]


OpenFeign
---

# 1 抛砖

Feign是一个不再维护的开源http client

OpenFeign是开源http client，类似retrofit
- https://github.com/OpenFeign/feign
- 本示例就是纯OpenFeign的demo，其中SpringMvcContract是从spring-cloud-openfeign拷出来的
    - 为了支持接口里用@RequestMapping这些spring注解，替代默认的RequestLine等

spring-cloud-openfeign在feign基础上增加了RequestMapping，负载均衡等特性的开源库
- https://github.com/spring-cloud/spring-cloud-openfeign


# 2 springcloud-loadbalancer

用于为负载均衡提供可选择的实例列表
```
public class WatchableServiceInstanceListSupplier implements ServiceInstanceListSupplier {

    private final String serviceId;

    private final WatchableDiscoveryClient watchableDiscoveryClient;

    public WatchableServiceInstanceListSupplier(
            String serviceId,
            WatchableDiscoveryClient watchableDiscoveryClient) {
        this.serviceId = serviceId;
        this.watchableDiscoveryClient = watchableDiscoveryClient;
    }

    @Override
    public String getServiceId() {
        return serviceId;
    }

    @Override
    public Flux<List<ServiceInstance>> get() {
        return watchableDiscoveryClient.getWatchableInstances(serviceId);
    }
}
或者
@Bean
@Primary
ServiceInstanceListSupplier serviceInstanceListSupplier() {
    return new ServiceInstanceListSupplier() {
        @Override
        public String getServiceId() {
            return "server";
        }

        @Override
        public Flux<List<ServiceInstance>> get() {
            return Flux.just(Arrays.asList(
                    new DefaultServiceInstance(getServiceId() + "1", getServiceId(), "localhost", 8090, false),

                    new DefaultServiceInstance(getServiceId() + "2", getServiceId(), "localhost", 9092, false),

                    new DefaultServiceInstance(getServiceId() + "3", getServiceId(), "localhost", 9999, false)));
        }
    };
}
```