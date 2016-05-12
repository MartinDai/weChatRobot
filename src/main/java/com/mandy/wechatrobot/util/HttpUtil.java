package com.mandy.wechatrobot.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public final class HttpUtil {

	/** 上传文件时定义数据分隔符(可以随意更改) **/
	private static String boundary = "thisIsBoundary";
	/** 上传文件时配合分隔符使用 **/
	private static String twoHyphens = "--";
	/** 上传文件时结尾标识 **/
	private static String end = "\r\n";
	
    private HttpUtil(){}
	/**
	 * 发起http post请求，支持添加文件参数
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @param fileMaps
	 *            文件map,key文件对应name,value为文件路径
	 * @param parameters
	 *            其他字符串参数
	 * @return 请求返回值
	 */
	public static String post(String requestUrl,
			Map<String, String> fileMaps, Map<String, String> parameters) {
		String result = null;
		try {
			/* 初始化连接 */
			HttpURLConnection httpUrlConn = initHttpURLConnection(requestUrl,
					"POST");
			DataOutputStream dos = new DataOutputStream(
					httpUrlConn.getOutputStream());
			/* 输出文件数据 */
			if (fileMaps != null) {
				for (Entry<String, String> entry : fileMaps.entrySet()) {
					writeFileParams(dos, entry.getKey(), entry.getValue());
				}
			}

			/* 输出字符串数据 */
			if (parameters != null) {
				for (Entry<String, String> entry : parameters.entrySet()) {
					writeStringParams(dos, entry.getKey(), entry.getValue());
				}
			}

			/* 请求结束 */
			paramsEnd(dos);
			/* 关闭输出流 */
			dos.close();

			/* 获取请求返回的输入流 */
			InputStream inputStream = httpUrlConn.getInputStream();
			/* 将返回的输入流转换成字符串 */
			result = getStringFromInputStream(inputStream);
			httpUrlConn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 发起http get请求，支持添加文件参数
	 * 
	 * @param requestUrl
	 *            请求地址
	 * @return 请求返回值
	 */
	public static String get(String requestUrl) {
		String result = null;
		try {
			/* 初始化连接 */
			HttpURLConnection httpUrlConn = initHttpURLConnection(requestUrl,
					"GET");
			/* 获取请求返回的输入流 */
			InputStream inputStream = httpUrlConn.getInputStream();
			/* 将返回的输入流转换成字符串 */
			result = getStringFromInputStream(inputStream);
			httpUrlConn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 初始化connection的一些必须设置
	 * 
	 * @param requestUrl
	 *            请求的地址
	 * @param type
	 *            请求类型
	 * @return HttpURLConnection
	 */
	private static HttpURLConnection initHttpURLConnection(String requestUrl,
			String type) throws Exception {
		URL url = new URL(requestUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		/* 允许Input、Output，不使用Cache */
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setUseCaches(false);
		/* 连接超时为10秒 */
		conn.setConnectTimeout(10000);
		/* 设置请求类型 */
		conn.setRequestMethod(type);
		if (type.equalsIgnoreCase("POST")) {
			/* setRequestProperty */
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("Charset", "UTF-8");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
		}
		return conn;
	}

	/**
	 * 请求输出字符串数据
	 * 
	 * @param dos
	 *            输出流对象
	 * @param name
	 *            参数name
	 * @param value
	 *            参数值
	 * @throws Exception
	 */
	private static void writeStringParams(DataOutputStream dos, String name,
			String value) throws Exception {
		dos.writeBytes(twoHyphens + boundary + end);
		dos.writeBytes("Content-Disposition:form-data;name=\"" + name + "\""
				+ end);
		dos.writeBytes(end);
		dos.write(value.getBytes("UTF-8"));
		dos.writeBytes(end);
	}

	/**
	 * 请求输出文件数据
	 * 
	 * @param dos
	 *            输出流对象
	 * @param name
	 *            参数name
	 * @param fileUrlStr
	 *            文件路径
	 * @throws Exception
	 */
	private static void writeFileParams(DataOutputStream dos, String name,
			String fileUrlStr) throws Exception {
		/* 建立文件获取连接 */
		URL fileUrl = new URL(fileUrlStr);
		URLConnection fileConn = fileUrl.openConnection();
		fileConn.setReadTimeout(10000);
		fileConn.setDoOutput(true);
		/* 获取文件后缀名 */
		String fileExt = fileUrlStr.substring(fileUrlStr.lastIndexOf("."));
		/* 根据文件后缀名获取contentType */
		String contentType = getContentType(fileExt);
		/* 获取文件名 */
		String fileName = fileUrlStr.substring(fileUrlStr.lastIndexOf("/") + 1,
				fileUrlStr.lastIndexOf("."));
		/* 请求开始 */
		dos.writeBytes(twoHyphens + boundary + end);
		dos.writeBytes(String.format("Content-Disposition:form-data;name=\""
				+ name + "\";filename=\"%s%s\"" + end, fileName, fileExt));
		dos.writeBytes(String.format("Content-Type:%s" + end, contentType));
		dos.writeBytes(end);
		/* 取得文件的InputStream */
		DataInputStream dis = new DataInputStream(fileConn.getInputStream());
		/* 设置每次写入8kb */
		byte[] buf = new byte[1024 * 8];
		int length = 0;
		/* 从文件读取数据至缓冲区 */
		while ((length = dis.read(buf)) != -1) {
			/* 将数据写入DataOutputStream中 */
			dos.write(buf, 0, length);
		}
		dos.writeBytes(end);
		dis.close();
	}

	/**
	 * 请求输出结尾
	 */
	private static void paramsEnd(DataOutputStream dos) throws Exception {
		dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
		dos.writeBytes(end);
	}

	/**
	 * 把返回的输入流转换成字符串
	 */
	private static String getStringFromInputStream(InputStream inputStream)
			throws Exception {
		BufferedReader bufferedReader = new BufferedReader(
				new InputStreamReader(inputStream, "UTF-8"));
		StringBuffer buffer = new StringBuffer();
		String str = null;
		while ((str = bufferedReader.readLine()) != null) {
			buffer.append(str);
		}

		/* 释放资源 */
		bufferedReader.close();
		inputStream.close();
		inputStream = null;
		return buffer.toString();
	}

	/**
	 * 根据内容类型返回后缀名
	 * 
	 * @param fileExt
	 *            文件后缀名
	 * @return 对应ContentType
	 */
	private static String getContentType(String fileExt) {
		String contentType = "text/html; charset=UTF-8";
		if (fileExt != null && fileExt.length() > 0) {
			if (".jpg".equalsIgnoreCase(fileExt))
				contentType = "image/jpeg";
			else if (".amr".equalsIgnoreCase(fileExt))
				contentType = "application/octet-stream";
			else if (".mp3".equalsIgnoreCase(fileExt))
				contentType = "audio/mp3";
			else if (".mp4".equalsIgnoreCase(fileExt))
				contentType = "video/mp4";
			else
				contentType = "application/octet-stream";
		}
		return contentType;
	}

}
