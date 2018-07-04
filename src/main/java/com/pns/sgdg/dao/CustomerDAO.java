package com.pns.sgdg.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.pns.sgdg.entity.Customer;
import com.pns.sgdg.model.Me;
import com.pns.sgdg.utility.ResourceUtil;

@Repository
public class CustomerDAO extends BaseDAO<Customer> {

	public Me getMe(long customerId) {
		return jdbc.queryForObject(ResourceUtil.getResouce("get-me"), new BeanPropertyRowMapper<>(Me.class),
				customerId);

	}

}
