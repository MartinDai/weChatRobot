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
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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

            vertx.createHttpServer()
                    .requestHandler(router)
                    .listen(webConfig.getApp().getPort())
                    .onSuccess(server -> log.info("HTTP server started on port " + server.actualPort()));
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String configPath = System.getProperty(CONFIG_KEY);
        if (StringUtils.isBlank(configPath)) {
            configPath = "config.yml";
        }

        Vertx vertx = Vertx.vertx();
        ConfigStoreOptions storeOptions = new ConfigStoreOptions()
                .setType("file")
                .setFormat("yaml")
                .setConfig(new JsonObject().put("path", configPath));

        ConfigRetrieverOptions retrieverOptions = new ConfigRetrieverOptions().addStore(storeOptions);

        ConfigRetriever retriever = ConfigRetriever.create(vertx, retrieverOptions);
        retriever.getConfig()
                .onSuccess(config -> {
                    log.info("load config success:" + config.toString());
                    DeploymentOptions options = new DeploymentOptions().setConfig(config);
                    vertx.deployVerticle(new MainVerticle(), options);
                })
                .onFailure(Throwable::printStackTrace);
    }
}
