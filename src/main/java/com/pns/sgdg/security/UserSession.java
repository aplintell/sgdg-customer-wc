package com.pns.sgdg.security;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

public class UserSession {

	@Autowired
	HttpServletRequest request;

	private long id;
	private String loginId;
	private int roleId;

	@PostConstruct
	public void init() {
		CookieUtil.CookieData cookieData = CookieUtil.getUserCookie(request);
		if (cookieData != null) {
			this.id = cookieData.getId();
			this.loginId = cookieData.getLoginId();
			this.roleId = cookieData.getRoleId();
		}
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

}
