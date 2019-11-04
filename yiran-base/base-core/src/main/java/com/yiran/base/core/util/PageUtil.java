package com.yiran.base.core.util;

import java.util.Iterator;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

/**
 * Pageable sort 转换工具
 */
public class PageUtil {

	public static String pageableSort(Sort sort) {
		if (null == sort) {
			return "";
		}
		StringBuilder sb = new StringBuilder();
		Iterator<Order> it = sort.iterator();
		while (it.hasNext()) {
			Order order = it.next();
			sb.append(order.getProperty()).append(" ").append(order.getDirection());
			if (it.hasNext()) {
				sb.append(",");
			}
		}
		return sb.toString();
	}
}
