package com.yiran.base.gateway.filter;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.google.common.util.concurrent.RateLimiter;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

/**
 * 请求限流过滤器 也有一个开源的组件
 * https://github.com/marcosbarbero/spring-cloud-zuul-ratelimit
 */
@Component
public class RateLimiterFilter extends ZuulFilter {

	private static final RateLimiter RETELIMITER = RateLimiter.create(1000);// 每秒放1000个token

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		// 限流要在所有pre类型过滤器之前
		return FilterConstants.SERVLET_DETECTION_FILTER_ORDER - 1;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {

		RequestContext requestContext = RequestContext.getCurrentContext();
		/**
		 * 尝试获取token, 没有则会失败 注意: rateLimiter.tryAcquire(500,
		 * TimeUnit.MILLISECONDS)是判断500ms内是否能拿到令牌, 并不是真会阻塞1s 它是根据令牌生成速率和当前时间做对比,
		 * 比如100个令牌/每秒, 在前100ms内到达101个请求, 说明令牌耗尽, 需要等待至少900ms才能拿到令牌, 所以没有等待必要, 立刻失败
		 */
		if (!RETELIMITER.tryAcquire()) {
			requestContext.setSendZuulResponse(false); // 过滤该请求, 不对其进行路由
			requestContext.setResponseStatusCode(HttpStatus.ACCEPTED.value());
			requestContext.setResponseBody("繁忙中... 请稍后再试");
		}
		return null;
	}
}
