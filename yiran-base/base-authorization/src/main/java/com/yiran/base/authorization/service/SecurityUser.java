package com.yiran.base.authorization.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yiran.base.system.object.Role;
import com.yiran.base.system.object.User;

public class SecurityUser extends User implements UserDetails {

	private static final long serialVersionUID = 1L;

	private boolean accountNonExpired = true;
	private boolean accountNonLocked = true;
	private boolean credentialsNonExpired = true;

	public SecurityUser(User user) {
		if (user != null) {
			this.setId(user.getId());
			this.setAccount(user.getAccount());
			this.setPassword(user.getPassword());
			this.setName(user.getName());
			this.setEmail(user.getEmail());
			this.setMobile(user.getMobile());
			this.setEnabled(user.getEnabled());
			this.setCreateTime(user.getCreateTime());
			this.setRoles(user.getRoles());
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		List<Role> roles = this.getRoles();
		if (roles != null) {
			for (Role role : roles) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getCode());
				authorities.add(authority);
			}
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return this.getAccount();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return this.getEnabled();
	}

	public boolean isAccountNonExpired() {
		return accountNonExpired;
	}

	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isCredentialsNonExpired() {
		return credentialsNonExpired;
	}

	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}
}
