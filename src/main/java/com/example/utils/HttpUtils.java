package com.example.utils;

import com.alibaba.fastjson.JSONObject;
import com.example.constants.HttpConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 *
 * 一分钟弄懂HTTPS过程：
 * 	在过去的几年里，业界巨头Google利用旗下Chrome浏览器大力推行HTTPS，没有使用SSL/TLS进行加密的网站一律被标记为不安全。
 * 	作为一个开发人员，连HTTPS原理都不懂的话是注定要被鄙视的。
 *
 * 	简化步骤如下：1.客户端发起请求；
 * 				2.服务端返回证书；
 * 				3.客户端从验证证书得到服务端的公钥；
 * 				4.客户端生成随机数，并用公钥加密后发送给服务端；
 * 				5.服务器根据随机数生成对称密钥；
 * 				6.用对称密钥加密数据传输；
 *
 */
public class HttpUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class); // 日志记录



	/**
	 * 发送GET/POST请求，返回String
	 * @param method 必须传值
	 * @param contentType default:applicaton/json
	 * @param url	必须传值
	 * @param params
	 * @return
	 */
	public static String send(String method, String contentType, String url, String params) {
		if ("GET".equals(method)) {
			return get(url);
		}
		
		if ("POST".equals(method)) {
			return post(url, params, contentType);
		}
		return null;
	}
	
	/**
	 * httpPost
	 * @param url 路径
	 * @param jsonParam 参数
	 * @return
	 */
	public static JSONObject httpPost(String url, JSONObject jsonParam) {
		return httpPost(url, jsonParam, false);
	}

	/**
	 * post请求
	 * @param url 地址
	 * @param jsonParam 参数
	 * @param noNeedResponse 不需要返回结果
	 * @return
	 */
	public static JSONObject httpPost(String url, JSONObject jsonParam, boolean noNeedResponse) {
		JSONObject jsonResult = null;
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost method = new HttpPost(url);
		try {
			if (null != jsonParam) {
				StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			HttpResponse result = client.execute(method);
			url = URLDecoder.decode(url, "UTF-8");
			if (result.getStatusLine().getStatusCode() == 200) {
				String str = "";
				try {
					str = EntityUtils.toString(result.getEntity());
					if (noNeedResponse) {
						return null;
					}
					jsonResult = JSONObject.parseObject(str);
				} catch (Exception e) {
					LOGGER.error("post请求提交失败:" + url, e);
				}
			}
		} catch (IOException e) {
			LOGGER.error("post请求提交失败:" + url, e);
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				LOGGER.error("链接关闭异常！");
			}
		}
		return jsonResult;
	}

	/**
	 * 发送get请求
	 * @param url 路径
	 * @return
	 */
	public static JSONObject httpGet(String url) {
		JSONObject jsonResult = null;
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(url);
			HttpResponse response = client.execute(request);

			int statusCode = response.getStatusLine().getStatusCode();
			LOGGER.info("httpResponseCode = " + statusCode);
			if (statusCode == HttpStatus.SC_OK) {
				String strResult = EntityUtils.toString(response.getEntity());
				jsonResult = JSONObject.parseObject(strResult);
			} else {
				LOGGER.error("get请求失败:" + url + " code:" + statusCode);
			}
		} catch (Exception e) {
			LOGGER.error("get请求失败:" + url, e);
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				LOGGER.error("链接关闭异常！");
			}
		}
		return jsonResult;
	}

    
    public static String getUrlParamsByMap(Map<String, Object> map) {
        if (map == null) {  
            return "";  
        }  
        StringBuffer sb = new StringBuffer();  
        for (Map.Entry<String, Object> entry : map.entrySet()) {  
            sb.append(entry.getKey() + "=" + entry.getValue());  
            sb.append("&");  
        }  
        String s = sb.toString();  
        if (s.endsWith("&")) {  
            s = StringUtils.substringBeforeLast(s, "&");
        }
        return s;  
    } 
    
	/**
	 * 发送get请求，返回字符串
	 * @param url 路径
	 * @return 返回字符串
	 */
	public static String get(String url) {
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpGet request = new HttpGet(url);

			Builder builder = RequestConfig.custom();
			builder.setConnectTimeout(HttpConstant.HTTP_CONNECT_TIMEOUT);
			builder.setConnectionRequestTimeout(HttpConstant.HTTP_CONNECT_REQUEST_TIMEOUT);
			builder.setSocketTimeout(HttpConstant.HTTP_SOCKET_TIMEOUT);

			request.setConfig(builder.build());

			HttpResponse response = client.execute(request);

			int statusCode = response.getStatusLine().getStatusCode();
			String responseContent = EntityUtils.toString(response.getEntity());
			LOGGER.info("status code :{} content:{} ", statusCode, responseContent);
			if (HttpStatus.SC_OK == statusCode) {
				return responseContent;
			}
			return null;
			
		} catch (IOException | ParseException e) {
			LOGGER.error("请求失败 " , e);
			return null;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				LOGGER.error("链接关闭异常！",e);
			}
		}
	}
	
	
	/**
	 * 发送POST请求，返回字符串
	 * @param url
	 * @param param 默认json格式参数
	 * @param contentType default:application/json
	 * @return
	 */
	public static String post(String url, String param,String contentType)  {
		if (StringUtils.isBlank(contentType)) {
			contentType = "application/json";
		}
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpPost method = new HttpPost(URLDecoder.decode(url, "UTF-8"));
			
			if (StringUtils.isNotBlank(param)) {
				StringEntity entity = new StringEntity(param, "UTF-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType(contentType);
				method.setEntity(entity);
			}
			HttpResponse result = client.execute(method);
			
			int statusCode = result.getStatusLine().getStatusCode();
			String responseContent = EntityUtils.toString(result.getEntity());
			LOGGER.info("status code :{} content:{} ", statusCode, responseContent);
			
			if (HttpStatus.SC_OK == statusCode) {
				return responseContent;
			} 
			return null;
		} catch (IOException e) {
			LOGGER.error("请求失败",e);
			return null;
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				LOGGER.error("链接关闭异常！",e);
			}
		}
		
	}
	
	
	/**
	 * 发送POST请求，返回字符串
	 * @param url
	 * @param jsonParam
	 * @return
	 */
	public static String post(String url, String jsonParam)  {
		
		CloseableHttpClient client = HttpClients.createDefault();
		try {
			HttpPost method = new HttpPost(URLDecoder.decode(url, "UTF-8"));
			
			if (StringUtils.isNotBlank(jsonParam)) {
				StringEntity entity = new StringEntity(jsonParam, "UTF-8");
				entity.setContentEncoding("UTF-8");
				entity.setContentType("application/json");
				method.setEntity(entity);
			}
			HttpResponse result = client.execute(method);
			
			int statusCode = result.getStatusLine().getStatusCode();
			LOGGER.info("status code = " + statusCode);
			
			if (HttpStatus.SC_OK == statusCode) {
				return EntityUtils.toString(result.getEntity());
			} 
			LOGGER.error("post请求失败 url = {} code = {}" , url , statusCode);
			return null;
		} catch (IOException e) {
			LOGGER.error("post请求失败 url = {}");
			e.printStackTrace();
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				LOGGER.error("链接关闭异常！");
			}
		}
		
		
		return null;
	}
	
	public static JSONObject HttpMultiPartPost(String actionUrl, Map<String, Object> params, Map<String, File> files) throws IOException
    {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setConnectTimeout(60000);
        conn.setReadTimeout(180000); // 缓存的最长时间，180秒
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", CHARSET);
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Object> entry : params.entrySet())
        {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }
        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
        
        // 发送文件数据
        if (files != null)
        {
            for (Map.Entry<String, File> file : files.entrySet())
            {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                // name是post中传参的键 filename是文件的名称
                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1)
                {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());
            }
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
        }
        outStream.close();
        
        //接收参数的服务器返回
        String line = null;
        String response = "";
        InputStream in = null;
        JSONObject jSONObject = null;
        
        // 得到响应码
        int res = conn.getResponseCode();
        if(res == 200) {
            in = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while((line = reader.readLine()) != null) {  
            	response += line + "\n";  
            }
            jSONObject = JSONObject.parseObject(response);
        }
        else {
        	LOGGER.info(response);
        	jSONObject = JSONObject.parseObject("");
        }
        
        in.close();
        conn.disconnect();
        return jSONObject;
        
    }


	/**
	 * http get 请求拼接url
	 * @param url
	 * @param params
	 * @return
	 */
	public static String getUrl(String url,HashMap<String, String> params) {
//		String url = requestUrl;
		// 添加url参数
		if (params != null) {
			Iterator<String> it = params.keySet().iterator();
			StringBuffer sb = null;
			while (it.hasNext()) {
				String key = it.next();
				String value = params.get(key);
				if (sb == null) {
					sb = new StringBuffer();
					sb.append("?");
				} else {
					sb.append("&");
				}
				sb.append(key);
				sb.append("=");
				sb.append(value);
			}
			url += sb.toString();
		}
		return url;
	}

}
