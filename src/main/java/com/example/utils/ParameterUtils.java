package com.example.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.constants.SemConstant;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


public class ParameterUtils {
	
	/**
	 * GET 请求？后面传过来的参数转换成JSON对象
	 * @param queryString
	 * @return
	 */
	public static JSONObject toJson(String queryString) {
		if (StringUtils.isBlank(queryString)) {
			return null;
		}
		
		String[] params = queryString.split("&");
		JSONObject result = new JSONObject();
		for (String param : params) {
			String[] kv = param.split("=");
			if (kv.length == 2) {
				String value = kv[1];
				try{
					value = URLDecoder.decode(value,"utf8");
				}catch(Exception e){
					e.printStackTrace();
				}
				result.put(kv[0], value);
			}
		}
		return result.isEmpty() ? null : result;
	}
	
	
	/**
	 * 删除queryString里面无用的标签
	 * @param queryString
	 * @param valRegex
	 * @return
	 */
	public static JSONObject toJson(String queryString, String... valRegex) {
		if (StringUtils.isBlank(queryString)) {
			return null;
		}
		
		if (null != valRegex && valRegex.length > 0) {
			for (String regex : valRegex) {
				queryString = queryString.replace(regex, SemConstant.STRING_EMPTY);
			}
		}
		
		return toJson(queryString);
	}
	
	/**
	 * URL queryString 转 Map ,头条专用请不要修改
	 * @param queryString
	 * @param valueRegex value标签( = 后面的值 {value} , __value__ 比如这种情况就可以填写{valRarget="_",valReplacement=""} 这样处理后就会把value里面的__去除掉)
	 * @param valReplacement 
	 * @return
	 */
	public static final Map<String, String> toMap(String queryString, String valRarget, String valReplacement)  {
		if (StringUtils.isBlank(queryString)) {
			return null;
		}
		if (StringUtils.isBlank(valRarget)) {
			valRarget = valReplacement = SemConstant.STRING_EMPTY;
		}
		Map<String, String> paramMap = new HashMap<>();
		String[] paramList = queryString.split("&");
		for (String param : paramList) {
			String[] keyVal = param.split("=");
			if (keyVal.length == 2) {
				String value =keyVal[1]. replace(valRarget, valReplacement);
				try {
					value = URLDecoder.decode(value,"utf8");
				}catch(Exception e){
					e.printStackTrace();
				}
				paramMap.put(keyVal[0], value);
			}
		}
		return paramMap.isEmpty() ? null : paramMap;
	}
	
}
