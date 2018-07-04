package com.pns.sgdg.model;

import java.util.List;

public class Me {

	private long customerId;
	private String name;
	private List<Long> permissions;
	private String status;

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Long> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Long> permissions) {
		this.permissions = permissions;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
