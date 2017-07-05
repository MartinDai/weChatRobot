package com.doodl6.wechatrobot.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.doodl6.wechatrobot.constant.AppConstants;
import org.apache.commons.lang.StringUtils;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Formatter;

/** 微信相关工具类 */
public final class WeChatUtil {

	/** 微信接口访问标记 **/
	private static String accessToken = null;
	/** 缓存时间 **/
	private static Long cacheTime = null;
    
	private WeChatUtil(){}
	/**
	 * 验证微信消息合法性
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) throws Exception {
		if (signature == null || timestamp == null || nonce == null) {
			return false;
		}
		String[] arr = new String[] { AppConstants.TOKEN, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (String str : arr) {
			content.append(str);
		}
		String tmpStr;
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		byte[] digest = md.digest(content.toString().getBytes("UTF-8"));
		tmpStr = byteToHex(digest);
		return tmpStr.equals(signature);
	}

	/**
	 * 获取与微信通信的标记
	 */
	public static String getAccessToken() throws Exception {
		long currentTime = System.currentTimeMillis() / 1000;
		if (!StringUtils.isEmpty(accessToken) && cacheTime != null && (currentTime - cacheTime) < 3600) {
			return accessToken;
		}
		String requestUrl = Constants.ACCESS_TOKEN_URL.replace("APPID", AppConstants.APP_ID).replace("APPSECRET",
				AppConstants.APP_SECRET);
		String result = HttpUtil.get(requestUrl);
		if (null == result) {
			return null;
		}
		JSONObject obj = JSON.parseObject(result);
		accessToken = obj.getString("access_token");
		cacheTime = currentTime;
		return accessToken;
	}

	public static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}
}
