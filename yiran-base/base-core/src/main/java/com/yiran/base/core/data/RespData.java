package com.yiran.base.core.data;

public class RespData<T> extends BaseRespData {

	public RespData() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	protected T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
