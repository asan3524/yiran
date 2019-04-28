package org.springframework.cloud.sleuth.instrument.web.client.feign;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;

import com.netflix.loadbalancer.Server;
import com.yiran.base.core.data.Transaction;
import com.yiran.base.ribbon.balancer.YiranCachingSpringLoadBalancerFactory;
import com.yiran.redis.cache.RedisCacheComponent;

import feign.Client;
import feign.Request;
import feign.Response;

public class YiranTraceLoadBalancerFeignClient extends TraceLoadBalancerFeignClient {

	public static final String YIRAN_BASE_RIBBON_STATUS_TRANSACTIONA_ID = "YIRAN_BASE_RIBBON_STATUS_TRANSACTIONA_ID_";
	private static final Logger Logger = LoggerFactory.getLogger(YiranTraceLoadBalancerFeignClient.class);

	private YiranCachingSpringLoadBalancerFactory lbClientFactory;
	private RedisCacheComponent cacheComponent;
	private String myserviceid;

	public YiranTraceLoadBalancerFeignClient(Client delegate, YiranCachingSpringLoadBalancerFactory lbClientFactory,
			SpringClientFactory clientFactory, BeanFactory beanFactory, RedisCacheComponent cacheComponent,
			String serviceid) {
		super((Client) new TraceFeignObjectWrapper(beanFactory).wrap(delegate), lbClientFactory, clientFactory,
				beanFactory);
		this.lbClientFactory = lbClientFactory;
		this.cacheComponent = cacheComponent;
		this.myserviceid = serviceid;
	}

	@Override
	public Response execute(Request request, Request.Options options) throws IOException {

		String server_id = null;
		boolean hasTransaction = false;

		URI asUri = URI.create(request.url());
		String clientName = asUri.getHost();

		Map<String, Collection<String>> headers = request.headers();
		if (!headers.isEmpty() && headers.containsKey(Transaction.YIRAN_BASE_HEADER_TRANSACTION)) {
			Collection<String> transactions = headers.get(Transaction.YIRAN_BASE_HEADER_TRANSACTION);

			if (null != transactions && !transactions.isEmpty()) {
				String transaction = transactions.iterator().next();

				if (null != transaction) {

					server_id = cacheComponent.hashGet(YIRAN_BASE_RIBBON_STATUS_TRANSACTIONA_ID + transaction,
							myserviceid, String.class);

					if (null != server_id) {
						// 根据事务选定服务，通知rule选择当前事务指定的服务器
						lbClientFactory.setKey(server_id);
						hasTransaction = true;
						Logger.info("status request for transaction : {} to server {} ", transaction, server_id);
					}

				}

			}

		}

		// 不是根据事务选定服务，通知rule选择已经提前选定的服务器
		// 或者已失效，服务器会重新生成事务ID并通过response返回
		if (!hasTransaction) {
			// 提前选定服务器
			Server server = lbClientFactory.create(clientName).getLoadBalancer().chooseServer(null);
			if (null != server) {
				server_id = server.getId();
				lbClientFactory.setKey(server_id);
			} else {
				// 未找到服务
			}
		}

		Response response = super.execute(request, options);
		headers = response.headers();

		if (!headers.isEmpty() && headers.containsKey(Transaction.YIRAN_BASE_HEADER_TRANSACTION)) {
			Collection<String> transactions = headers.get(Transaction.YIRAN_BASE_HEADER_TRANSACTION);

			if (null != transactions && !transactions.isEmpty()) {
				String transaction = transactions.iterator().next();

				if (null != transaction) {

					Collection<String> validitys = headers.get(Transaction.YIRAN_BASE_HEADER_VALIDITY);

					if (null != validitys && !validitys.isEmpty()) {
						String validity = validitys.iterator().next();

						try {
							Integer vs = Integer.parseInt(validity);

							// 如果响应中有transaction信息，则记录当前transaction与server信息到redis
							cacheComponent.hashPut(YIRAN_BASE_RIBBON_STATUS_TRANSACTIONA_ID + transaction, myserviceid,
									server_id, vs);

							Logger.info("status request for server {} transaction{} validity {}", server_id,
									transaction, validity);
						} catch (NumberFormatException e) {

							Logger.error(
									"status request for transaction : {} to server {} response has no validity, can not save to redis",
									transaction, server_id);
						}
					}

				}
			}
		}
		return response;
	}
}
