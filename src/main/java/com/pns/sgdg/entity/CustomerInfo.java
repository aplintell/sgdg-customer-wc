package com.pns.sgdg.entity;

import com.pns.sgdg.annotation.Column;
import com.pns.sgdg.annotation.Key;
import com.pns.sgdg.annotation.Table;

@Table(name = "customer_info")
public class CustomerInfo extends BaseEntity {

	@Key(isAI = false, name = "customer_id")
	private long customerId;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "first_name")
	private String firstName;

	public CustomerInfo(long id, String lastName, String firstName) {
		super();
		this.customerId = id;
		this.lastName = lastName;
		this.firstName = firstName;
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

}
