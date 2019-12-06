package com.lyl.layuiadmin.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class CookieUtils {
	/**
	 *  * 设置cookie
	 * 
	 * @param response
	 *             
	 * @param name
	 *              cookie名字  
	 * @param value
	 *            cookie值  
	 * @param maxAge
	 *            cookie生命周期  以秒为单位  
	 */
	public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		if (maxAge > 0)
			cookie.setMaxAge(maxAge);
		response.addCookie(cookie);
	}
	/**
	 * 删除cookie
	 * @param response
	 * @param name
	 */
	public static void deleteCookie(HttpServletResponse response, String name) {
		Cookie cookie = new Cookie(name, null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	/**
	 *   根据名字获取cookie  
	 * 
	 * @param request
	 *             
	 * @param name
	 * @return  
	 */
	public static Cookie getCookieByName(HttpServletRequest request, String name) {
		Map<String, Cookie> cookieMap = ReadCookieMap(request);
		if (cookieMap.containsKey(name)) {
			Cookie cookie = (Cookie) cookieMap.get(name);
			return cookie;
		} else {
			return null;
		}
	}

	/**
	 * 将cookie封装到Map里面  
	 * 
	 * @param request
	 * @return  
	 */
	private static Map<String, Cookie> ReadCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (null != cookies) {
			for (Cookie cookie : cookies) {
				cookieMap.put(cookie.getName(), cookie);
			}
		}
		return cookieMap;
	}

	/**
	 * 保存用户cookie
	 * @param response
	 * @param name
	 * @param value
     * @param maxAge
     */
	public static void saveCookie(HttpServletResponse response, String name, String value, int maxAge) {
		//cookie的有效期
		long validTime = System.currentTimeMillis() + (maxAge * 5000);
		//MD5加密用户详细信息
		String cookieValueWithMd5 =getMD5(value);
		//将要被保存的完整的Cookie值
		String cookieValue = value + ":" + cookieValueWithMd5;
		//再一次对Cookie的值进行BASE64编码
		String cookieValueBase64 = new String(Base64.encode(cookieValue.getBytes()));
		//开始保存Cookie
		Cookie cookie = new Cookie(name, cookieValueBase64);
		//存两年(这个值应该大于或等于validTime)
		cookie.setMaxAge(60 * 60 * 24 * 10);
		//cookie有效路径是网站根目录
		cookie.setPath("/");
		//向客户端写入
		response.addCookie(cookie);
	}

	/**
	 * 读取cookie看是否保存有用户名
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
     */
	public static String readCookieForLogon(HttpServletRequest request)
			throws UnsupportedEncodingException {
		Cookie cookie = getCookieByName(request, "jun");
		if(cookie != null) {
			String cookieValue = cookie.getValue();
			//如果cookieValue不为空,才执行下面的代码
			//先得到的CookieValue进行Base64解码
			String cookieValueAfterDecode = new String(Base64.decode(cookieValue), "utf-8");
			String cookieValues[] = cookieValueAfterDecode.split(":");
			if (cookieValues.length != 2) {
				return null;
			}

			//取出cookie中的用户名,并到数据库中检查这个用户名,
			String username = cookieValues[0];
			String md5ValueInCookie = cookieValues[1];
			String md5ValueFromUser = getMD5(username);
			if (md5ValueFromUser.equals(md5ValueInCookie)) {
				return username;
			}else {
				return null;
			}
		}
		return null;
	}

	/**
	 * 获取Cookie组合字符串的MD5码的字符串
	 * @param value
	 * @return
     */
	public static String getMD5(String value) {
		String result = null;
		try{
			byte[] valueByte = value.getBytes();
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(valueByte);
			result = toHex(md.digest());
		} catch (NoSuchAlgorithmException e1){
			e1.printStackTrace();
		}
		return result;
	}

	/**
	 * 将传递进来的字节数组转换成十六进制的字符串形式并返回
	 * @param buffer
	 * @return
     */
	private static String toHex(byte[] buffer){
		StringBuffer sb = new StringBuffer(buffer.length * 2);
		for (int i = 0; i < buffer.length; i++){
			sb.append(Character.forDigit((buffer[i] & 0xf0) >> 4, 16));
			sb.append(Character.forDigit(buffer[i] & 0x0f, 16));
		}
		return sb.toString();
	}

}
