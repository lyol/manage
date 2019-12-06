package com.lyl.layuiadmin.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import eu.bitwalker.useragentutils.Version;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 	获取IP工具类
 */
public class RequestUtils {

	private static final String ORIGINAL_REQUEST_URI = "aitou.framework.request.original.uri";
	private static Logger logger = LoggerFactory.getLogger(RequestUtils.class);

	/**
	 * 打印出HttpServletRequest的内容，包括：http的头、以及Body信息。
	 * @param request
	 * @return
	 */
	public static String printRequest(HttpServletRequest request) {
		if(request != null) {
			StringBuilder requestDetail = new StringBuilder();
			fillHeaders(request, requestDetail);
			if (request instanceof TraceableHttpServletRequestWrapper) {
				requestDetail.append(((TraceableHttpServletRequestWrapper) request).getBodyContent());
			} else {
				fillBody(request, requestDetail);
			}
			return requestDetail.toString();
		} else {
			return "";
		}
	}

	protected static void fillHeaders(HttpServletRequest request, StringBuilder requestDetail) {
		requestDetail.append("\nRequest URL: ").append(request.getRequestURL()).append("\n");
		requestDetail.append("Request Method: ").append(request.getMethod()).append("\n");
		requestDetail.append("Rquest Headers:\n");
		Enumeration<String> headers = request.getHeaderNames();
		while (headers.hasMoreElements()) {
			String header = (String) headers.nextElement();
			requestDetail.append(header).append(": ").append(request.getHeader(header)).append("\n");
		}
		requestDetail.append("\n");// 换行再加一个空行
	}

	public static void fillBody(HttpServletRequest request, StringBuilder requestDetail) {
		Map<String, String[]> map = request.getParameterMap();
		Set<String> keys = map.keySet();
		if(logger.isDebugEnabled()) {
			logger.debug("request里的参数的个数:{}", map.size());
		}
		if (keys != null && !keys.isEmpty()) {
			Iterator<String> keyItr = keys.iterator();
			int index = 0;
			while (keyItr.hasNext()) {
				String key = (String) keyItr.next();
				if(index ++ >0) {
					requestDetail.append("&");
				}
				arrayToString(key, (String[]) map.get(key), requestDetail);
			}
		}
	}

	protected static void arrayToString(String key, String[] values, StringBuilder requestDetail) {
		for (int i = 0, l = values.length; i < l; i++) {
			if (i > 0) {
				requestDetail.append("&");
			}
			requestDetail.append(key).append("=").append(values[i]);
		}
	}

	/**
	 * 判断是否是Ajax请求
	 * @param request
	 * @return
	 */
	public static boolean isAjaxRequest(HttpServletRequest request) {
		return (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").indexOf(
				"XMLHttpRequest") > -1);
	}

	/**
	 * 判断是否是文件上传的请求。
	 * @param request
	 * @return
	 */
	public static boolean isFileUploadRequest(ServletRequest request) {
		String contentType = request.getContentType();
		if(StringUtils.isBlank(contentType)) {
			return false;
		}
		return contentType.contains("multipart/form-data");
	}

	/**
	 * 得到请求方的真实IP地址，注意由于前面采用NAT协议，通过request.getRemoteAddr()无法得到真实的IP，因此提供此封装。
	 */
	public static String getClientIp(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
			if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
				ip = request.getHeader("WL-Proxy-Client-IP");
				if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
					ip = request.getRemoteAddr();
				}
			}
		} else {
			int firstCommaIndex = ip.indexOf(",");
			if(firstCommaIndex > 0) {
				ip = ip.substring(0, firstCommaIndex).trim();
			}
		}
		return ip;
	}

	public static String getOriginalRequestURI(HttpServletRequest request) {
		String originalURI = (String) request.getAttribute(ORIGINAL_REQUEST_URI);
		if(originalURI != null && !originalURI.isEmpty()) {
			return originalURI;
		} else {
			return request.getRequestURI();
		}
	}

	public static void setOriginalRequestURI(HttpServletRequest request) {
		request.setAttribute(ORIGINAL_REQUEST_URI, request.getRequestURI());
	}

	public static String getCookie(HttpServletRequest request, String cookieName) {
		Cookie[] cookies = request.getCookies();
		if(cookies != null && cookies.length > 0)
			for(Cookie cookie: request.getCookies()) {
				if(cookie.getName().equals(cookieName)) {
					return cookie.getValue();
				}
			}
		return null;
	}

	public static void setCookie(HttpServletResponse response, String cookieName, String cookieValue) {
		try {
			if (cookieValue == null) {
				cookieValue = "";
			}
			Cookie cookie = new Cookie(cookieName, cookieValue);
			cookie.setPath("/");
			response.addCookie(cookie);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断是否是移动设备发出的请求
	 * @param request
	 */
	public static boolean isMobileRequest(HttpServletRequest request) {
		String userAgent = request.getHeader("user-agent").toLowerCase();
		if((userAgent.contains("android")||userAgent.contains("iphone")) &&userAgent.contains("mobile")){
			return true;
		}
		return false;
	}

	/**
	 * 获取发起请求的浏览器名称
	 */
	public static String getBrowserName(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(header);
		Browser browser = userAgent.getBrowser();
		return browser.getName();
	}

	/**
	 * 获取发起请求的浏览器版本号
	 */
	public static String getBrowserVersion(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(header);
		//获取浏览器信息
		Browser browser = userAgent.getBrowser();
		//获取浏览器版本号
		Version version = browser.getVersion(header);
		return version.getVersion();
	}

	/**
	 * 获取发起请求的操作系统名称
	 */
	public static String getOsName(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		UserAgent userAgent = UserAgent.parseUserAgentString(header);
		OperatingSystem operatingSystem = userAgent.getOperatingSystem();
		return operatingSystem.getName();
	}

}
