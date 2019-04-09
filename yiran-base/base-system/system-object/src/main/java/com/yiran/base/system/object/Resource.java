package com.yiran.base.system.object;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author lishibang 资源模型
 */
public class Resource {

	public static Resource copy(Resource original, Resource update) {
		// TODO Auto-generated constructor stub
		if (null == update.getName()) {
			update.setName(original.getName());
		}
		if (null == update.getMethod()) {
			update.setMethod(original.getMethod());
		}
		if (null == update.getUrl()) {
			update.setUrl(original.getUrl());
		}
		if (null == update.getRemark()) {
			update.setRemark(original.getRemark());
		}
		if (null == update.getCreateTime()) {
			update.setCreateTime(original.getCreateTime());
		}
		return update;
	}

	private String id;
	private String name;
	/**
	 * GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE;
	 */
	private String method;
	private String url;
	private String remark;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
