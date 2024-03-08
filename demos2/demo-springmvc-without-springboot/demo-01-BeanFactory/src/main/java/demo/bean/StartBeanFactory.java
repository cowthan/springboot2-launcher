package demo.bean;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * .
 */
public class StartBeanFactory {
    public static void main(String[] args) {
        DefaultListableBeanFactory bf = new DefaultListableBeanFactory();
        bf.setAllowBeanDefinitionOverriding(false);
        bf.setAllowCircularReferences(true);

        bf.addBeanPostProcessor(new DaoInstantiationAwareBeanPostProcessor());

        // 注册 UserDao
        BeanDefinitionBuilder userDaoBuilder =
                BeanDefinitionBuilder.genericBeanDefinition(UserDao.class);
        userDaoBuilder.setInitMethodName("init");
        userDaoBuilder.setDestroyMethodName("onDestroy");
        bf.registerBeanDefinition("userDao",userDaoBuilder.getRawBeanDefinition());

        // 注册 ProfileDao
        ProfileDao profileDao = new ProfileDao();
        bf.registerSingleton("profileDao", profileDao);

        // 注册UserService，并注入依赖
        BeanDefinitionBuilder userServiceBuilder =
                BeanDefinitionBuilder.genericBeanDefinition(UserService.class);
        //-- 注入 userDao
        userServiceBuilder.addPropertyReference("userDao","userDao");
        //-- 注入 profileDao
        userServiceBuilder.addAutowiredProperty("profileDao");

//        userServiceBuilder.addConstructorArgReference(beanName);
//        userServiceBuilder.addConstructorArgValue(Object);
//        userServiceBuilder.addDependsOn(beanName);
//        userServiceBuilder.addPropertyValue(name, value);


        //-- register
        GenericBeanDefinition abd = (GenericBeanDefinition) userServiceBuilder.getRawBeanDefinition();
        bf.registerBeanDefinition("userService", abd);


        UserService us = bf.getBean(UserService.class);
        us.saySth();

        bf.destroyBean(bf.getBean(UserDao.class));
    }
}
