package com.yiran.base.core.data;

import org.springframework.data.domain.Pageable;

public class PageRequestData<T> extends ReqData<T> {

	private Pageable pageable;

	public Pageable getPageable() {
		return pageable;
	}

	public void setPageable(Pageable pageable) {
		this.pageable = pageable;
	}
}
