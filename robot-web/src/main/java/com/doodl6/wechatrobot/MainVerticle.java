package com.doodl6.wechatrobot;

import com.doodl6.wechatrobot.config.WebConfig;
import com.doodl6.wechatrobot.handler.MainHandler;
import com.doodl6.wechatrobot.util.JsonUtil;
import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MainVerticle extends AbstractVerticle {

    private static final String CONFIG_KEY = "config";

    @Override
    public void start() {
        try {
            Router router = Router.router(vertx);

            router.route().handler(StaticHandler.create("static"));

            String json = config().toString();
            WebConfig webConfig = JsonUtil.jsonToObj(json, WebConfig.class);
            Route messageRoute = router.route("/weChat/receiveMessage");
            messageRoute.handler(new MainHandler(vertx, webConfig.getWechat(), webConfig.getKeyword()));

            int port = webConfig.getApp().getPort();
            vertx.createHttpServer()
                    .requestHandler(router)
                    .listen(port, listen -> {
                        if (listen.succeeded()) {
                            log.info("HTTP server started on port: {}", port);
                        } else {
                            listen.cause().printStackTrace();
                            System.exit(1);
                        }
                    });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String configPath = System.getProperty(CONFIG_KEY);
        if (StringUtils.isBlank(configPath)) {
            configPath = "config.yml";
        }

        VertxOptions options = new VertxOptions();
        // 设置事件循环线程的最大执行时间
        options.setMaxEventLoopExecuteTime(TimeUnit.SECONDS.toNanos(10));
        Vertx vertx = Vertx.vertx(options);
        ConfigStoreOptions storeOptions = new ConfigStoreOptions()
                .setType("file")
                .setFormat("yaml")
                .setConfig(new JsonObject().put("path", configPath));

        ConfigRetrieverOptions retrieverOptions = new ConfigRetrieverOptions().addStore(storeOptions);

        ConfigRetriever retriever = ConfigRetriever.create(vertx, retrieverOptions);
        retriever.getConfig()
                .onSuccess(config -> {
                    log.info("load config success:{}", config.toString());
                    DeploymentOptions deploymentOptions = new DeploymentOptions().setConfig(config);
                    vertx.deployVerticle(new MainVerticle(), deploymentOptions);
                })
                .onFailure(Throwable::printStackTrace);
    }
}
