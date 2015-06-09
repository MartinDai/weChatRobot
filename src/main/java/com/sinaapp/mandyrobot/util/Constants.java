package com.sinaapp.mandyrobot.util;

/** 常量类 */
public class Constants {

	/* GET或POST（必须大写，不可更改） */
	public static final String GET = "GET";
	public static final String POST = "POST";

	/* 微信请求消息类型（由微信方规定，不可更改） */
	/** 文本 */
	public static final String REQ_TEXT_TYPE = "text";
	/** 事件 */
	public static final String REQ_EVENT_TYPE = "event";
	/** 订阅 */
	public static final String REQ_SUBSCRIBE_TYPE = "subscribe";
	/** 取消订阅 */
	public static final String REQ_UNSUBSCRIBE_TYPE = "unsubscribe";

	/* 微信返回消息类型（由微信方规定，不可更改） */
	/** 文本 */
	public static final String RESP_TEXT_TYPE = "text";
	/** 图文 */
	public static final String RESP_NEWS_TYPE = "news";

	/* 图灵机器人返回数据类型状态码(官方固定) */
	/** 文本 */
	public static final Integer TEXT_CODE = 100000;
	/** 列车 */
	public static final Integer TRAIN_CODE = 305000;
	/** 航班 */
	public static final Integer FLIGHT_CODE = 306000;
	/** 链接类 */
	public static final Integer LINK_CODE = 200000;
	/** 新闻 */
	public static final Integer NEWS_CODE = 302000;
	/** 菜谱、视频、小说 */
	public static final Integer MENU_CODE = 308000;
	/** key的长度错误（32位） */
	public static final Integer LENGTH_WRONG_CODE = 40001;
	/** 请求内容为空 */
	public static final Integer EMPTY_CONTENT_CODE = 40002;
	/** key错误或帐号未激活 */
	public static final Integer KEY_WRONG_CODE = 40003;
	/** 当天请求次数已用完 */
	public static final Integer NUMBER_DONE_CODE = 40004;
	/** 暂不支持该功能 */
	public static final Integer NOT_SUPPORT_CODE = 40005;
	/** 服务器升级中 */
	public static final Integer UPGRADE_CODE = 40006;
	/** 服务器数据格式异常 */
	public static final Integer DATA_EXCEPTION_CODE = 40007;

	/** 获取access_token的接口地址 */
	public final static String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

	/** 图灵机器人接口地址 */
	public final static String TURING_API_URL = "http://www.tuling123.com/openapi/api";

}
