package com.mandy.wechatrobot;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.component.AbstractLifeCycle;
import org.eclipse.jetty.util.component.LifeCycle;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * 启动jetty入口
 * Created by daixiaoming on 2017/1/8.
 */
public class StartJetty {

    private static String WEBAPP = "Start";

    private static int JETTY_SERVER_PORT = 8080;

    private static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public static void main(String[] args) {
        try {
            Server server = new Server(JETTY_SERVER_PORT);
            //添加启动完成输出监听
            server.addLifeCycleListener(new AbstractLifeCycle.AbstractLifeCycleListener() {
                @Override
                public void lifeCycleStarted(LifeCycle event) {
                    System.err.println("----------weChatRobot Webapp is started! PORT:" + JETTY_SERVER_PORT + "----------");
                }
            });

            String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + "**/" + StartJetty.class.getSimpleName() + ".class";
            Resource resource = resolver.getResources(pattern)[0];
            String resourcePath = resource.getFile().getAbsolutePath().replaceAll("target.*$", "") + "src/main/webapp";
            WebAppContext context = new WebAppContext();
            context.setDescriptor(resourcePath + "/WEB-INF/web.xml");
            context.setResourceBase("file:" + resourcePath);
            context.setContextPath("/");
            context.setClassLoader(Thread.currentThread().getContextClassLoader());
            server.setHandler(context);

            server.setStopAtShutdown(true);
            server.start();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
