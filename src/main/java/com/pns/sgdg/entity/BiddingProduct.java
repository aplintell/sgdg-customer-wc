package com.pns.sgdg.entity;

import java.util.Date;

import com.pns.sgdg.annotation.Column;
import com.pns.sgdg.annotation.Key;
import com.pns.sgdg.annotation.Table;

@Table(name = "bidding_product")
public class BiddingProduct extends BaseEntity {

	@Key(isAI = true, name = "bidding_product_id")
	private long bidding_product_id;

	@Column(name = "name")
	private String name;

	@Column(name = "parsing_url")
	private String parsingUrl;

	@Column(name = "initial_price")
	private double initialPrice;

	@Column(name = "price_step")
	private double priceStep;

	@Column(name = "description")
	private String description;

	@Column(name = "information")
	private String information;

	@Column(name = "specification")
	private String specification;

	@Column(name = "start_date")
	private Date startDate;

	@Column(name = "end_date")
	private Date endDate;

	@Column(name = "category_id")
	private int categoryId;

	public BiddingProduct() {
		super();
	}

	public BiddingProduct(String name, String parsingUrl, double initialPrice, double priceStep, String description,
			String information, String specification, Date startDate, Date endDate, int categoryId) {
		super();
		this.name = name;
		this.parsingUrl = parsingUrl;
		this.initialPrice = initialPrice;
		this.priceStep = priceStep;
		this.description = description;
		this.information = information;
		this.specification = specification;
		this.startDate = startDate;
		this.endDate = endDate;
		this.categoryId = categoryId;
	}

	public String getParsingUrl() {
		return parsingUrl;
	}

	public void setParsingUrl(String parsingUrl) {
		this.parsingUrl = parsingUrl;
	}

	public long getBidding_product_id() {
		return bidding_product_id;
	}

	public void setBidding_product_id(long bidding_product_id) {
		this.bidding_product_id = bidding_product_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getInitialPrice() {
		return initialPrice;
	}

	public void setInitialPrice(double initialPrice) {
		this.initialPrice = initialPrice;
	}

	public double getPriceStep() {
		return priceStep;
	}

	public void setPriceStep(double priceStep) {
		this.priceStep = priceStep;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public String getSpecification() {
		return specification;
	}

	public void setSpecification(String specification) {
		this.specification = specification;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

}
