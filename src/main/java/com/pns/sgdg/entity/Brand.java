package com.pns.sgdg.entity;

import com.pns.sgdg.annotation.Column;
import com.pns.sgdg.annotation.Key;
import com.pns.sgdg.annotation.Table;

@Table(name = "brand")
public class Brand extends BaseEntity {

	@Key(isAI = true, name = "brand_id")
	private int brandId;

	@Column(name = "name")
	private String name;

	@Column(name = "image")
	private byte[] image;

	@Column(name = "priority")
	private int priority;

	@Column(name = "status")
	private String status;

	public Brand() {
	}

	public Brand(int brandId, String name, byte[] image, int priority, String status) {
		super();
		this.brandId = brandId;
		this.name = name;
		this.image = image;
		this.priority = priority;
		this.status = status;
	}

	public int getBrandId() {
		return brandId;
	}

	public void setBrandId(int brandId) {
		this.brandId = brandId;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
