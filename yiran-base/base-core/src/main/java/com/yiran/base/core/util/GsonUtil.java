package com.yiran.base.core.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {
	public static Gson dateGson(String datePattern) {
		return new GsonBuilder().setDateFormat(datePattern).create();
	}

	/**
	 * 默认日期格式"yyyy-MM-dd HH:mm:ss"
	 * 
	 * @return
	 */
	public static Gson dateGson() {
		return dateGson(CommonUtils.DATE_TIME_FMT);
	}
}
