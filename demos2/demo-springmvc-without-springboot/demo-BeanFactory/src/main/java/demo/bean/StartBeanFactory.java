package demo.bean;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * .
 */

@Configuration
@ComponentScan(basePackages = {"demo.bean"})
public class StartBeanFactory {
    public static void main(String[] args) {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        bf.setAllowBeanDefinitionOverriding(false);
        bf.setAllowCircularReferences(true);

        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext();
        ac.setAllowBeanDefinitionOverriding(false);
        ac.setAllowCircularReferences(true);
        ac.register(StartBeanFactory.class); // 让ComponentScan生效
        ac.refresh();

        UserService us = ac.getBean(UserService.class);
        us.saySth();
    }
}
