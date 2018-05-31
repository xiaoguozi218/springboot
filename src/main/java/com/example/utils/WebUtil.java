package com.example.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Created by peichunting on 18/2/23.
 */
public class WebUtil {

    private static final byte[] UTF8_BOM_BYTES = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
    static ResourceBundle conf = ResourceBundle.getBundle("config");
    private static Log logger = LogFactory.getLog(WebUtil.class);

    /**
     * 获取当前网络ip
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = request.getHeader("x-forwarded-for");
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0
                || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            if (ipAddress.equals("127.0.0.1")
                    || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    if (logger.isErrorEnabled()) {
                        logger.error("Oh...", e);
                    }
                }
                ipAddress = inet.getHostAddress();
            }
        }
        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (ipAddress != null && ipAddress.length() > 15) {
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }
        return ipAddress;
    }

    public static String getHost(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getLocalName() + ":" + request.getLocalPort();
    }

    @SuppressWarnings("restriction")
    public static String bean2Xml(Object rc)
            throws JAXBException {
        javax.xml.bind.JAXBContext context;
        try {
            context = javax.xml.bind.JAXBContext.newInstance(rc.getClass());
            javax.xml.bind.Marshaller mar = context.createMarshaller();
            mar.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT,
                    true);
            mar.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8");

            StringWriter writer = new StringWriter();
            mar.marshal(doEmptyStringAdapter(rc), writer);
            return writer.toString();
        } catch (JAXBException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @SuppressWarnings("restriction")
    public static Object xml2Bean(String xml, Class c) {
        javax.xml.bind.JAXBContext context;
        try {
            context = javax.xml.bind.JAXBContext.newInstance(c);
            javax.xml.bind.Unmarshaller unmar = context.createUnmarshaller();
            return unmar.unmarshal(new StringReader(removeUTF8BOM(xml)));
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 移除UTF-8文件中的BOMtouch
     *
     * @param xmlText
     * @return
     */
    public static String removeUTF8BOM(String xmlText) {
        byte[] bytes = xmlText.getBytes();
        boolean containsBOM = bytes.length > 3
                && bytes[0] == UTF8_BOM_BYTES[0]
                && bytes[1] == UTF8_BOM_BYTES[1]
                && bytes[2] == UTF8_BOM_BYTES[2];
        if (containsBOM) {
            xmlText = new String(bytes, 3, bytes.length - 3);
        }
        return xmlText;
    }

    public static String getProperties(String key) {
        return conf.getString(key);
    }

    public static ResourceBundle getConfig() {
        return conf;
    }

    public static final byte[] readBytes(InputStream is, int contentLen) {
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen
                            - readLen);
                    if (readLengthThisTime == -1) {// Should not happen.
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return message;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[]{};
    }

    public static byte[] readBytes(InputStream var0, long var1) throws IOException {
        if(var1 > 1048576L) {
            throw new IOException("File too large");
        } else {
            BufferedInputStream var3 = null;
            if(var0 instanceof BufferedInputStream) {
                var3 = (BufferedInputStream)var0;
            } else {
                var3 = new BufferedInputStream(var0);
            }

            if(var1 <= 0L) {
                var1 = 10240L;
            }

            byte[] var4 = new byte[(int)var1];
            int var7 = 0;

            byte[] var8;
            for(int var6 = var3.read(var4, var7, var4.length - var7); var6 != -1; var6 = var3.read(var4, var7, var4.length - var7)) {
                var7 += var6;
                if(var4.length == var7) {
                    var8 = new byte[var4.length * 2];
                    System.arraycopy(var4, 0, var8, 0, var4.length);
                    var4 = var8;
                }
            }

            var3.close();
            var0.close();
            if(var7 != var4.length) {
                var8 = new byte[var7];
                System.arraycopy(var4, 0, var8, 0, var7);
                var4 = var8;
            }

            return var4;
        }
    }


    @SuppressWarnings({"unchecked", "restriction", "rawtypes"})
    public static Object doEmptyStringAdapter(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        Class c = obj.getClass();
        for (Field field : fields) {
            if (field.getAnnotation(javax.xml.bind.annotation.XmlElement.class) == null) {
                continue;
            }
            String getName = "get";
            String setName = "set";
            String fieldName = field.getName();
            char[] cs = fieldName.toCharArray();
            cs[0] -= 32;
            getName += String.valueOf(cs);
            setName += String.valueOf(cs);

            if (field.getType().equals(String.class)) {
                try {
                    Method getMethod = c.getMethod(getName);
                    String value = (String) getMethod.invoke(obj);
                    if (value == null) {
                        continue;
                    }
                    if ("".equals(value) || "".equals(value.trim())) {
                        Method setMethod = c.getMethod(setName, String.class);
                        setMethod.invoke(obj, new Object[]{null});
                    }
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                    continue;
                }

            } else if (field.getAnnotation(javax.xml.bind.annotation.XmlElementWrapper.class) != null) {//集合类型的field
                try {
                    Method getMethod = c.getMethod(getName);
                    Collection fieldValues = (Collection) getMethod.invoke(obj);
                    if (fieldValues == null || fieldValues.isEmpty()) {
                        continue;
                    }
                    for (Object fieldValue : fieldValues) {
                        doEmptyStringAdapter(fieldValue);
                    }
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                    continue;
                }

            } else if (field.getType().getAnnotation(javax.xml.bind.annotation.XmlRootElement.class) != null) {//字段是一个对象
                try {
                    Method getMethod = c.getMethod(getName);
                    Object fieldValue = (Object) getMethod.invoke(obj);
                    if (fieldValue == null) {
                        continue;
                    }
                    doEmptyStringAdapter(fieldValue);
                } catch (IllegalArgumentException | IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
                    continue;
                }

            }

        }
        return obj;
    }

    public static Calendar getCalendarWithoutHMSM(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    public static String replaceDomainAndPort(String domain, String port, String url) {
        String urlBak = "";

        if (url.indexOf("//") != -1) {
            String[] splitTemp = url.split("//");
            urlBak = splitTemp[0] + "//";

            if (port != null) {
                urlBak = urlBak + domain + ":" + port;
            } else {
                urlBak = urlBak + domain;
            }

            if ((splitTemp.length >= 1) && (splitTemp[1].indexOf("/") != -1)) {
                String[] urlTemp2 = splitTemp[1].split("/");

                if (urlTemp2.length > 1) {
                    for (int i = 1; i < urlTemp2.length; i++) {
                        urlBak = urlBak + "/" + urlTemp2[i];
                    }
                }
            }
        }
        return urlBak;
    }
}
