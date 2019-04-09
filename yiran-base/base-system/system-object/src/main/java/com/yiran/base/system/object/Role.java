package com.yiran.base.system.object;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author lishibang 角色模型
 */
public class Role {

	public static Role copy(Role original, Role update) {
		// TODO Auto-generated constructor stub
		if (null == update.getName()) {
			update.setName(original.getName());
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
	private String code;
	private String name;
	private String remark;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;
	private List<Resource> resources;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}
}
