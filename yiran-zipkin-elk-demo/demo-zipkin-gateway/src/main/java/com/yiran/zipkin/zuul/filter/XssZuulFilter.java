package com.yiran.zipkin.zuul.filter;

import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.http.ServletInputStreamWrapper;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * 基于Zuul的 XSS过滤器 
 */
@Component
@RefreshScope
public class XssZuulFilter extends ZuulFilter {
	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	// 自定义过滤器执行的顺序，数值越大越靠后执行，越小就越先执行
	@Override
	public int filterOrder() {
		return FilterConstants.PRE_DECORATION_FILTER_ORDER - 2;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	// 执行过滤逻辑
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		try {
			InputStream in = ctx.getRequest().getInputStream();
			String body = StreamUtils.copyToString(in, Charset.forName("UTF-8"));
			String newBody = JsoupUtil.clean(body);
			final byte[] reqBodyBytes = newBody.getBytes();
			ctx.setRequest(new HttpServletRequestWrapper(request) {

				@Override
				public ServletInputStream getInputStream() throws IOException {
					return new ServletInputStreamWrapper(reqBodyBytes);
				}

				@Override
				public int getContentLength() {
					return reqBodyBytes.length;
				}

				@Override
				public long getContentLengthLong() {
					return reqBodyBytes.length;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private void setUnauthorizedResponse(RequestContext requestContext) {
		@SuppressWarnings("serial")
		Map<String, Object> res = new HashMap<String, Object>() {{put("msg", "参数非法");}};
		requestContext.setResponseStatusCode(HttpStatus.BAD_REQUEST.value()); //400
		requestContext.setResponseBody(JSONObject.toJSONString(res));
	}
}