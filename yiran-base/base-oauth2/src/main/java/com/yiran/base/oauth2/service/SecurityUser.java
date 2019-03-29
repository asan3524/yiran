package com.yiran.base.oauth2.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.yiran.base.system.object.RoleQo;
import com.yiran.base.system.object.UserQo;

public class SecurityUser extends UserQo implements UserDetails {
	private static final long serialVersionUID = 1L;

	public SecurityUser(UserQo user) {
		if (user != null) {
			this.setId(user.getId());
			this.setName(user.getName());
			this.setEmail(user.getEmail());
			this.setPassword(user.getPassword());
			this.setSex(user.getSex());
			this.setCreated(user.getCreated());
			this.setRoles(user.getRoles());
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		List<RoleQo> roles = this.getRoles();
		if (roles != null) {
			for (RoleQo role : roles) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
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
		return super.getName();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
