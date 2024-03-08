package demo.mvc;

import javax.servlet.ServletContext;

import demo.mvc.config.WebApplicationConfig;
import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * MainServer
 */
public class StartWebServer {

    public static void main(String[] args) {
        new StartWebServer();
        System.out.println("服务已启动：http://localhost:8088/test");
        System.out.println("服务已启动：http://localhost:8088/index.html");
    }

    public StartWebServer(){
        Tomcat tomcat = new Tomcat();//创建tomcat对象
        tomcat.setPort(8088); //设置端口
        tomcat.getConnector();

        //创建web容器上下文
        Context context = tomcat.addContext("", null);
        try {
            //注册前端控制器
            DispatcherServlet dispatcherServlet = new DispatcherServlet(this.createApplicationContext(context.getServletContext()));
            Wrapper servlet = tomcat.addServlet(context, "dispatcherServlet", dispatcherServlet);
            servlet.setLoadOnStartup(1);
            servlet.addMapping("/*");

            tomcat.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建spring容器的应用上下文对象
     * @param servletContext WEB容器上下文对象
     * @return spring容器应用上下文对象
     */
    public WebApplicationContext createApplicationContext(
            ServletContext servletContext) {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.register(WebApplicationConfig.class); //加载配置类

        ctx.setServletContext(servletContext);
        ctx.refresh();
        ctx.registerShutdownHook();
        return ctx;
    }


}
