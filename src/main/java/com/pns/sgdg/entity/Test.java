package com.pns.sgdg.entity;

import com.pns.sgdg.annotation.Column;
import com.pns.sgdg.annotation.Key;
import com.pns.sgdg.annotation.Table;

@Table(name = "test")
public class Test extends BaseEntity {

	@Key(isAI = true, name = "test_id")
	private long testId;

	@Column(name = "name")
	private String name;

	public long getTestId() {
		return testId;
	}

	public void setTestId(long testId) {
		this.testId = testId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
