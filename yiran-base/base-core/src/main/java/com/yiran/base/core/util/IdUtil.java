package com.yiran.base.core.util;

import com.yiran.base.core.util.id.IdWorker;

/**
 * Id生成器
 */
public class IdUtil {
	private static IdWorker idWorker = new IdWorker(1);

	private IdUtil() {
	}

	public static Long generateId() {
		return idWorker.nextId();
	}
}
