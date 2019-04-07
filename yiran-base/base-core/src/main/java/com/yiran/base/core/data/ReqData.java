package com.yiran.base.core.data;

public class ReqData<T> extends BaseReqData {

	protected T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
