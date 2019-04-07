package com.yiran.base.core.data;

import java.util.List;

public class PageResponseData<T extends List<?>> extends RespData<T> {

	public PageResponseData() {
		// TODO Auto-generated constructor stub
		super();
	}

	@SuppressWarnings("rawtypes")
	public PageResponseData(PageRequestData pageRequestData) {
		// TODO Auto-generated constructor stub
		super();
		setPageNumber(pageRequestData.getPageable().getPageNumber());
		setPageSize(pageRequestData.getPageable().getPageSize());
		this.transaction = pageRequestData.getTransaction();
	}

	// 第几页
	private int pageNumber;
	// 每页多少条
	private int pageSize;

	// 总计多少条
	private long total;

	// 当前返回多少条
	private int number;

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public int getNumber() {
		return this.number;
	}

	public void setNumber(int number) {
		this.number = number;
	}
}
