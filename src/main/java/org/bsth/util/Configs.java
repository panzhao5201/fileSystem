package org.bsth.util;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * 配置类
 * @author zhao
 *
 */
@Component
public class Configs {
	public static String ROOTPATH;//文件根目录
	public static String MAXFILESIZE;//文件最大大小
	public static List<String> SUFFIXS;//可上传的文件类型
	public static JSONObject CONTENTTYPES;//content type 对照表
	
	public Configs() {
		super();
		Tools t = new Tools("resource.properties");
		ROOTPATH = t.getValue("rootPath");
		MAXFILESIZE = t.getValue("rootPath");
		SUFFIXS = Arrays.asList(t.getValue("suffixs").split(","));
		CONTENTTYPES = JSONObject.parseObject(t.getValue("ContentType"));
		System.out.println("初始化配置信息");
	}
}
