package com.pns.sgdg.dao;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.pns.sgdg.entity.Customer;
import com.pns.sgdg.model.CustomerBO;
import com.pns.sgdg.model.Me;
import com.pns.sgdg.model.SearchPagingResult;
import com.pns.sgdg.utility.PagingUtil;
import com.pns.sgdg.utility.ResourceUtil;

@Repository
public class CustomerDAO extends BaseDAO<Customer> {

	public Me getMe(long customerId) {
		return jdbc.queryForObject(ResourceUtil.getResouce("get-me"), new BeanPropertyRowMapper<>(Me.class),
				customerId);
	}

	public SearchPagingResult find(String firstName, String lastName, String loginId, String email, int page) {
		return PagingUtil.query(jdbc, CustomerBO.class, "find-customer-bo", "find-customer-bo-count",
				Stream.of("%" + loginId + "%", "%" + loginId + "%", "%" + firstName + "%", "%" + firstName + "%",
						"%" + lastName + "%", "%" + lastName + "%", "%" + email + "%", "%" + email + "%")
						.collect(Collectors.toList()),
				page);
	}

	public CustomerBO getById(long id) {
		return jdbc.queryForObject(ResourceUtil.getResouce("find-customer-by-id"),
				new BeanPropertyRowMapper<>(CustomerBO.class), id);
	}

}
