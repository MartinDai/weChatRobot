package com.doodl6.wechatrobot.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 图灵常量类
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TulingConstants {

    /* 图灵机器人返回状态码(官方固定) */
    /**
     * 成功
     */
    public static final Integer SUCCESS = 0;

    /**
     * 参数错误
     */
    public static final Integer PARAM_ERROR = 4000;

    /**
     * 没有请求次数
     */
    public static final Integer NO_API_TIMES = 4003;

    /**
     * 无解析结果
     */
    public static final Integer NO_RESULTS = 5000;

    /**
     * 暂不支持该功能
     */
    public static final Integer NOT_SUPPORT = 6000;

}
