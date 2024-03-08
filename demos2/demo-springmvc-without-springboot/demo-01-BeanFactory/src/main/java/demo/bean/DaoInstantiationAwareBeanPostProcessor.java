package demo.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;

/**
 * DaoInstantiationAwareBeanPostProcessor
 */
public class DaoInstantiationAwareBeanPostProcessor implements
        SmartInstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("userDao")) {
            System.out.println("BeanPostProcessor - postProcessBeforeInitialization");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (beanName.equals("userDao")) {
            System.out.println("BeanPostProcessor - postProcessAfterInitialization");
        }
        return bean;
    }


    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass,
                                                 String beanName) throws BeansException {
        if (beanName.equals("userDao")) {
            System.out.println("SmartInstantiationAwareBeanPostProcessor - postProcessBeforeInstantiation");
        }
        return null; // 返回null则不替换原始bean实例
    }


    /**
     * true if properties should be set on the bean; false if property population should be skipped. Normal
     * implementations should return true. Returning false will also prevent any subsequent InstantiationAwareBeanPostProcessor instances being invoked on this bean instance.
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName)
            throws BeansException {
        if (beanName.equals("userDao")) {
            System.out.println("SmartInstantiationAwareBeanPostProcessor - postProcessAfterInstantiation");
        }
        return true;
    }

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        if (beanName.equals("userDao")) {
            System.out.println("SmartInstantiationAwareBeanPostProcessor - postProcessProperties");
        }
        return null;
    }

}
