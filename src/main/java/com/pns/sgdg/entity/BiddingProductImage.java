package com.pns.sgdg.entity;

import com.pns.sgdg.annotation.Column;
import com.pns.sgdg.annotation.Key;
import com.pns.sgdg.annotation.Table;

@Table(name = "bidding_product_image")
public class BiddingProductImage extends BaseEntity {

	@Key(isAI = true, name = "image_id")
	private long imageId;

	@Column(name = "name")
	private String name;

	@Column(name = "bidding_product_id")
	private long biddingProductId = 0;

	public long getImageId() {
		return imageId;
	}

	public void setImageId(long imageId) {
		this.imageId = imageId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getBiddingProductId() {
		return biddingProductId;
	}

	public void setBiddingProductId(long biddingProductId) {
		this.biddingProductId = biddingProductId;
	}

}
