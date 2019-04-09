package com.yiran.base.system.object;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

/**
 * @author lishibang 用户模型
 */
public class User {

	public static User copy(User original, User update) {
		// TODO Auto-generated constructor stub
		if (null == update.getName()) {
			update.setName(original.getName());
		}
		if (null == update.getPassword()) {
			update.setPassword(original.getPassword());
		}
		if (null == update.getEmail()) {
			update.setEmail(original.getEmail());
		}
		if (null == update.getMobile()) {
			update.setMobile(original.getMobile());
		}
		if (null == update.getRemark()) {
			update.setRemark(original.getRemark());
		}
		if(null == update.getEnabled()) {
			update.setEnabled(original.getEnabled());
		}
		if (null == update.getCreateTime()) {
			update.setCreateTime(original.getCreateTime());
		}
		return update;
	}

	private String id;
	private String account;
	private String name;
	private String password;
	private String email;
	private String mobile;
	private String remark;
	private Boolean enabled;
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	private List<Role> roles = new ArrayList<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}
}
