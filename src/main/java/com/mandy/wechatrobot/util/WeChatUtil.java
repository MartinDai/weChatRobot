package com.mandy.wechatrobot.util;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Formatter;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

/** 微信相关工具类 */
public class WeChatUtil {

	/** 微信接口访问标记 **/
	private static String accessToken = null;
	/** 缓存时间 **/
	private static Long cacheTime = null;

	/**
	 * 验证微信消息合法性
	 */
	public static boolean checkSignature(String signature, String timestamp, String nonce) throws Exception {
		if (signature == null || timestamp == null || nonce == null) {
			return false;
		}
		String[] arr = new String[] { PropertiesLoader.TOKEN, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (String str : arr) {
			content.append(str);
		}
		String tmpStr = "";
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
		String requestUrl = Constants.ACCESS_TOKEN_URL.replace("APPID", PropertiesLoader.APP_ID).replace("APPSECRET",
				PropertiesLoader.APP_SECRET);
		String result = HttpUtil.get(requestUrl);
		if (null == result) {
			return null;
		}
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(result);
		accessToken = jsonNode.path("access_token").asText();
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
