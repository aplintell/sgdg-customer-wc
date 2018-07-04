package com.pns.sgdg.entity;

import java.util.Date;

import com.pns.sgdg.annotation.Column;

public class BaseEntity {

	@Column(name = "created_time", ignoreUpdate = true)
	private Date createdTime = new Date();

	@Column(name = "created_by", ignoreUpdate = true)
	private String createdBy = "SYSTEM";

	@Column(name = "updated_time")
	private Date updatedTime = new Date();

	@Column(name = "updated_by")
	private String updatedBy = "SYSTEM";

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

}
