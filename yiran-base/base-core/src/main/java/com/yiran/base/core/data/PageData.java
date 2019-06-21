package com.yiran.base.core.data;

import java.util.List;

public class PageData<T> {
	// 第几页
	private int page;
	// 每页多少条
	private int size;

	// 总计多少条
	private long total;

	// 当前返回多少条
	private int number;

	private List<T> content;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
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

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
}
