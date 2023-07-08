package com.doodl6.wechatrobot.util;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class HttpUtil {

    private static final Logger LOGGER = Logger.getLogger(HttpUtil.class.getName());

    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");

    private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient();

    public static String get(String url) {
        Request request = new Request.Builder().url(url).get().build();
        return executeRequest(request);
    }

    public static String post(String url, String requestBodyJson) {
        Request request = new Request.Builder().url(url).post(RequestBody.create(requestBodyJson, MEDIA_TYPE)).build();
        return executeRequest(request);
    }

    private static String executeRequest(Request request) {
        try {
            try (Response response = OK_HTTP_CLIENT.newCall(request).execute()) {
                int statusCode = response.code();
                if (statusCode != 200) {
                    throw new RuntimeException("接口响应失败，statusCode:" + statusCode + "，url:" + request.url());
                }
                ResponseBody responseBody = response.body();
                if (responseBody == null) {
                    throw new RuntimeException("请求未收到响应，url:" + request.url());
                }

                return responseBody.string();
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "api调用异常，url:" + request.url(), e);
            throw new RuntimeException(e);
        }
    }
}
