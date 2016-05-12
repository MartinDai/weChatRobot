package com.mandy.wechatrobot.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mandy.wechatrobot.vo.Article;
import com.mandy.wechatrobot.vo.NewsMessage;
import com.mandy.wechatrobot.vo.TextMessage;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;

public class MessageUtil {

	/** 扩展xstream，使其支持CDATA块 */
	private static XStream xstream = new XStream(new XppDriver() {
		public HierarchicalStreamWriter createWriter(Writer out) {
			return new PrettyPrintWriter(out) {
				// 对所有xml节点的转换都增加CDATA标记
				boolean cdata = true;

				@SuppressWarnings("rawtypes")
				public void startNode(String name, Class clazz) {
					super.startNode(name, clazz);
				}

				protected void writeText(QuickWriter writer, String text) {
					if (cdata) {
						writer.write("<![CDATA[");
						writer.write(text);
						writer.write("]]>");
					} else {
						writer.write(text);
					}
				}
			};
		}
	});

	/**
	 * 接收输入流并转换成字符串
	 */
	public static String streamConvertToStr(InputStream inputStream) throws IOException {
		byte[] bytes = new byte[1024 * 1024];
		int nRead = 1;
		int nTotalRead = 0;
		while (nRead > 0) {
			nRead = inputStream.read(bytes, nTotalRead, bytes.length - nTotalRead);
			if (nRead > 0)
				nTotalRead = nTotalRead + nRead;
		}
		return new String(bytes, 0, nTotalRead, "utf-8");
	}

	/** 解析微信发来的请求（XML） */
	@SuppressWarnings("unchecked")
	public static Map<String, String> parseXml(InputStream inputStream) throws Exception {

		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();
		Document document = reader.read(inputStream);
		/** 得到xml根元素 */
		Element root = document.getRootElement();
		/** 得到根元素的所有子节点 */
		List<Element> elementList = root.elements();
		/** 遍历所有子节点 */
		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}
		/** 释放资源 */
		inputStream.close();
		
		return map;
	}

	/**
	 * 对象转换成xml字符串
	 */
	public static <T> String ObjectToXml(T t) {
		xstream.alias("xml", t.getClass());
		if (t instanceof NewsMessage) {
			xstream.alias("item", new Article().getClass());
		}
		return xstream.toXML(t);
	}

	/**
	 * 处理图灵机器人返回的结果
	 */
	public static Object processTuRingResult(String result, String fromUserName, String toUserName) throws Exception {
		JSONObject rootObj = JSON.parseObject(result);
		int code = rootObj.getIntValue("code");
		if (Constants.TEXT_CODE.equals(code)) {
			TextMessage text = new TextMessage(fromUserName, toUserName, rootObj.getString("text"));
			return text;
		} else if (Constants.LINK_CODE.equals(code)) {
			TextMessage text = new TextMessage(fromUserName, toUserName, "<a href='" + rootObj.getString("url") + "'>"
					+ rootObj.getString("text") + "</a>");
			return text;
		} else if (Constants.NEWS_CODE.equals(code) || Constants.TRAIN_CODE.equals(code)
				|| Constants.FLIGHT_CODE.equals(code) || Constants.MENU_CODE.equals(code)) {
			NewsMessage news = new NewsMessage(fromUserName, toUserName);
			List<JSONObject> list = JSON.parseArray(rootObj.getString("list"), JSONObject.class);
			assembleNews(news, list, code);
			return news;
		} else if (Constants.LENGTH_WRONG_CODE.equals(code) || Constants.KEY_WRONG_CODE.equals(code)) {
			TextMessage text = new TextMessage(fromUserName, toUserName, "我现在想一个人静一静,请等下再跟我聊天");
			return text;
		} else if (Constants.EMPTY_CONTENT_CODE.equals(code)) {
			TextMessage text = new TextMessage(fromUserName, toUserName, "你不说话,我也没什么好说的");
			return text;
		} else if (Constants.NUMBER_DONE_CODE.equals(code)) {
			TextMessage text = new TextMessage(fromUserName, toUserName, "我今天有点累了,明天再找我聊吧！");
			return text;
		} else if (Constants.NOT_SUPPORT_CODE.equals(code)) {
			TextMessage text = new TextMessage(fromUserName, toUserName, "这个我还没学会,我以后会慢慢学的");
			return text;
		} else if (Constants.UPGRADE_CODE.equals(code)) {
			TextMessage text = new TextMessage(fromUserName, toUserName, "我经验值满了,正在升级中...");
			return text;
		} else if (Constants.DATA_EXCEPTION_CODE.equals(code)) {
			TextMessage text = new TextMessage(fromUserName, toUserName, "你都干了些什么,我怎么话都说不清楚了");
			return text;
		}
		return null;
	}

	/**
	 * 根据消息类型组装图文消息
	 */
	private static void assembleNews(NewsMessage news, List<JSONObject> list, int code) {
		String titleKey = "article";
		String descriptionKey = "article";
		if (Constants.MENU_CODE.equals(code)) {
			titleKey = "name";
			descriptionKey = "info";
		}
		List<Article> articles = new ArrayList<Article>();
		for (JSONObject obj : list) {
			Article article = new Article();
			if (Constants.TRAIN_CODE.equals(code)) {
				article.setTitle(obj.getString("trainnum") + " —— 开车时间:" + obj.getString("starttime"));
				article.setDescription(obj.getString("start") + "(" + obj.getString("starttime") + ")——>"
						+ obj.getString("terminal") + "(" + obj.getString("endtime") + ")");
			} else if (Constants.FLIGHT_CODE.equals(code)) {
				article.setTitle(obj.getString("flight") + " —— 起飞时间:" + obj.getString("starttime"));
				article.setDescription(obj.getString("route") + obj.getString("starttime") + "——>"
						+ obj.getString("endtime") + "\n航班状态:" + obj.getString("state"));
			} else {
				article.setTitle(obj.getString(titleKey));
				article.setDescription(obj.getString(descriptionKey));
			}
			article.setPicUrl(obj.getString("icon"));
			article.setUrl(obj.getString("detailurl"));
			articles.add(article);
			if (articles.size() == 10) {
				break;
			}
		}
		news.setArticles(articles);
		news.setArticleCount(articles.size());
	}
}
