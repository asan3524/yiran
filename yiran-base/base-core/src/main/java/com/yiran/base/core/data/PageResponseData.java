package com.yiran.base.core.data;

public class PageResponseData<T extends PageData<?>> extends RespData<T> {

	public PageResponseData() {
		// TODO Auto-generated constructor stub
		super();
	}

	@SuppressWarnings("rawtypes")
	public PageResponseData(PageRequestData pageRequestData) {
		// TODO Auto-generated constructor stub
		super();
		this.transaction = pageRequestData.getTransaction();
	}

}
