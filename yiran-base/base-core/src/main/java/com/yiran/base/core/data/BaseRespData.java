package com.yiran.base.core.data;

import java.util.Calendar;
import java.util.Date;

public class BaseRespData extends BaseData {
	public BaseRespData() {
		// TODO Auto-generated constructor stub
		this.timestamp = Calendar.getInstance().getTime();
	}

	protected int code;
	protected String message;
	protected Date timestamp;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public String toString() {
		return "BaseRespData [transaction=" + transaction + ", code=" + code + ", message=" + message + ", timestamp="
				+ timestamp + "]";
	}
}
