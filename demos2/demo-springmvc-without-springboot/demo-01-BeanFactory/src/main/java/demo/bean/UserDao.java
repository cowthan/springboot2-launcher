package demo.bean;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * .
 */
public class UserDao implements InitializingBean, DisposableBean,
        BeanNameAware, BeanFactoryAware {

    public UserDao(){
        System.out.println("LifeCircle - UserDao 构造方法");
    }

    public void saySth(){
        System.out.println("userDao - UserDao.saySth()");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("LifeCircle - UserDao InitializingBean.afterPropertiesSet()");
    }
    public void init() throws Exception {
        System.out.println("LifeCircle - UserDao init()");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("LifeCircle - UserDao DisposableBean.destroy()");
    }
    public void onDestroy() throws Exception {
        System.out.println("LifeCircle - UserDao onDestroy()");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println("Aware - BeanNameAware.setBeanName('" + name + "')");
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("Aware - BeanFactoryAware.setBeanFactory()");
    }
}
