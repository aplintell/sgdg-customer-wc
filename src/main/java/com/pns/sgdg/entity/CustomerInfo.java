package com.pns.sgdg.entity;

import java.util.Date;

import com.pns.sgdg.annotation.Column;
import com.pns.sgdg.annotation.Key;
import com.pns.sgdg.annotation.Table;
import com.pns.sgdg.common.constant.EnumConstant;

@Table(name = "customer_info")
public class CustomerInfo extends BaseEntity {

	@Key(isAI = false, name = "customer_id")
	private long customerId;

	@Column(name = "last_name")
	private String lastName = "";

	@Column(name = "first_name")
	private String firstName = "";

	@Column(name = "gender")
	private String gender = EnumConstant.Gender.FEMALE.toString();

	@Column(name = "phone")
	private String phone;

	@Column(name = "email")
	private String email;

	@Column(name = "dob")
	private Date dob;

	@Column(name = "address")
	private String address;

	@Column(name = "avatar")
	private String avatar;

	@Column(name = "front_id_card")
	private String frontIdCard;

	@Column(name = "back_id_card")
	private String backIdCard;

	@Column(name = "point")
	private int point;

	@Column(name = "customer_type")
	private String customerType;

	public CustomerInfo() {
		super();
	}

	public CustomerInfo(long customerId, String lastName, String firstName, String gender, String phone, String email,
			Date dob, String address, String avatar, String frontIdCard, String backIdCard, int point,
			String customerType) {
		super();
		this.customerId = customerId;
		this.lastName = lastName;
		this.firstName = firstName;
		this.gender = gender;
		this.phone = phone;
		this.email = email;
		this.dob = dob;
		this.address = address;
		this.avatar = avatar;
		this.frontIdCard = frontIdCard;
		this.backIdCard = backIdCard;
		this.point = point;
		this.customerType = customerType;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getFrontIdCard() {
		return frontIdCard;
	}

	public void setFrontIdCard(String frontIdCard) {
		this.frontIdCard = frontIdCard;
	}

	public String getBackIdCard() {
		return backIdCard;
	}

	public void setBackIdCard(String backIdCard) {
		this.backIdCard = backIdCard;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

}
