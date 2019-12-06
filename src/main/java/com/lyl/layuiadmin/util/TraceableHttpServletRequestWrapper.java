package com.lyl.layuiadmin.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;

/**
 * 可跟踪的HttpServletRequest，可以保留InputStream。但除两种情况：1、通过getParameter获得了变量；2、文件上传。
 */
public class TraceableHttpServletRequestWrapper extends HttpServletRequestWrapper {

	private static Logger logger = LoggerFactory.getLogger(TraceableHttpServletRequestWrapper.class);

	/**是否直接使用InputStream*/
	private boolean directUseInputStream;

	/**Http的参数被解析了*/
	private boolean parameterParsed;

	/**Http的Body的Copy*/
	private byte[] httpBody;
	/**
	 * @param request
	 */
	public TraceableHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	//由于参数的解析是需要将InputStream解析出来，因此需要在获得变量的地方打上标记，否则将会是每一个请求都会被Copy出来。
	public String getParameter(String name) {
		parameterParsed = true;
		return super.getParameter(name);
	}

	public Enumeration getParameterNames() {
		parameterParsed = true;
		return super.getParameterNames();
	}

	public String[] getParameterValues(String name) {
		parameterParsed = true;
		return super.getParameterValues(name);
	}

	public Map getParameterMap() {
		parameterParsed = true;
		return super.getParameterMap();
	}

	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	private void copyByteArray(byte b[], int len) {
		int length = 0;
		byte[] newArray;
		if (httpBody != null) {
			length = httpBody.length;
			newArray = new byte[length + len];
			System.arraycopy(httpBody, 0, newArray, 0, length);
		} else {
			newArray = new byte[len];
		}
		System.arraycopy(b, 0, newArray, length, len);
		httpBody = newArray;
	}

	public String getBodyContent() {
		if (directUseInputStream) {
			return new String(httpBody);
		} else {
		    StringBuilder sb = new StringBuilder();
			RequestUtils.fillBody((HttpServletRequest) getRequest(), sb);
			return sb.toString();
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		//说明：对于通过参数来解析的，那么已经将数据记录到参数中了，而对于文件上传没有必要记录当前Body的内容。
	    if(parameterParsed || RequestUtils.isFileUploadRequest(getRequest())) {
	        return getRequest().getInputStream();
	    } else {
			logger.warn(this.getRequestURI() + "直接使用到InputStream，而非通过getParameter等方法。");
			//TODO 目前采用InputStream中的内容进行System.arrayCopy，性能应该可以，但应该有更高效的方案，例如：通过Jackson的ByteSourceJsonBootstrapper中去获得，其缓存的_inputBuffer
			//由于正常情况下getInputStream()仅能被调用一次，因此这里直接进行判断了。
			directUseInputStream = true;
    		return new ServletInputStream() {
				@Override
				public boolean isFinished() {
					return false;
				}

				@Override
				public boolean isReady() {
					return false;
				}

				@Override
				public void setReadListener(ReadListener listener) {

				}

				final InputStream orginalInputStream = getRequest().getInputStream();
    			@Override
    			public int read() throws IOException {
    				return orginalInputStream.read();
    			}
    			@Override
    			public int read(byte b[], int off, int len) throws IOException {
    				int length = -1;
    				length = orginalInputStream.read(b, off, len);
    				if (length > 0)
    					copyByteArray(b, length);
    				return length;
    			}
    		};
	    }
	}
}