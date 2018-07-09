package com.pns.sgdg.utility;

import java.lang.reflect.Field;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.ReflectionUtils;

import com.pns.sgdg.annotation.DBConfig;

public class ConfigureValueProvider extends InstantiationAwareBeanPostProcessorAdapter {

	@Autowired
	JdbcTemplate jdbc;

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		ReflectionUtils.doWithFields(bean.getClass(), new ReflectionUtils.FieldCallback() {

			@Override
			public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
				DBConfig annotation = field.getAnnotation(DBConfig.class);
				if (annotation == null) {
					return;
				}

				if (annotation.value() == null || annotation.value().isEmpty()) {
					return;
				}

				String configValue;
				try {
					configValue = jdbc.queryForObject("SELECT value FROM db_config WHERE name = ?", String.class,
							annotation.value());
				} catch (EmptyResultDataAccessException e) {
					throw new RuntimeException(
							String.format("There is no config value for [%s] in table db_config", annotation.value()));
				}

				field.setAccessible(true);
				if (field.getType() == Integer.TYPE) {
					field.setInt(bean, Integer.parseInt(configValue));
				} else if (field.getType() == Long.TYPE) {
					field.setLong(bean, Long.parseLong(configValue));
				} else {
					field.set(bean, configValue);
				}
			}
		});

		return bean;
	}

}
