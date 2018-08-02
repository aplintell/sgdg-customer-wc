package com.pns.sgdg.entity;

import java.util.Date;

import com.pns.sgdg.annotation.Column;
import com.pns.sgdg.annotation.Key;
import com.pns.sgdg.annotation.Table;
import com.pns.sgdg.common.constant.EnumConstant;

@Table(name = "user")
public class User extends BaseEntity {

	@Key(isAI = true, name = "user_id")
	private long userId;

	@Column(name = "login_id")
	private String loginId;

	@Column(name = "password", ignoreUpdate = true)
	private String password;

	@Column(name = "status")
	private String status = EnumConstant.UserStatus.ACTIVE.toString();

	@Column(name = "salt", ignoreUpdate = true)
	private String salt;

	@Column(name = "role_id")
	private int roleId;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "gender")
	private String gender;

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

	public User() {
	}

	public User(long userId, String loginId, String status, int roleId, String lastName, String firstName,
			String gender, String phone, String email, Date dob, String address, String avatar, String frontIdCard,
			String backIdCard) {
		this.userId = userId;
		this.loginId = loginId;
		this.status = status;
		this.roleId = roleId;
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

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
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

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

}
