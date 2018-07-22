package com.pns.sgdg.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import com.pns.sgdg.entity.User;
import com.pns.sgdg.model.Me;
import com.pns.sgdg.utility.ResourceUtil;

@Repository
public class UserDAO extends BaseDAO<User> {

	public Me getMe(long userId) {
		return jdbc.queryForObject(ResourceUtil.getResouce("get-me-user"), new BeanPropertyRowMapper<>(Me.class),
				userId);
	}

}
