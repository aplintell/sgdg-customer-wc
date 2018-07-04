package com.pns.sgdg.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.mysql.jdbc.Statement;
import com.pns.sgdg.annotation.Column;
import com.pns.sgdg.annotation.Key;
import com.pns.sgdg.annotation.Table;
import com.pns.sgdg.common.constant.Constant;
import com.pns.sgdg.common.constant.EnumConstant;
import com.pns.sgdg.entity.BaseEntity;

public class BaseDAO<T extends BaseEntity> {

	private Class<T> clazz;

	@Autowired
	JdbcTemplate jdbc;

	public List<T> getAll() {
		clazz = getConcreteClass();
		Table table = clazz.getAnnotation(Table.class);
		return jdbc.query(String.format("SELECT * FROM %s", table.name()), new BeanPropertyRowMapper<>(clazz));
	}

	/*
	 * String value will be compare by "LIKE" instead of "="
	 */
	public List<T> findLike(T t) {
		try {
			clazz = getConcreteClass();
			Table table = clazz.getAnnotation(Table.class);

			// Build SQL Query
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("SELECT * FROM ");
			sqlStr.append(table.name());
			sqlStr.append(" WHERE ");
			boolean isFirstField = false;
			Field[] fields = clazz.getDeclaredFields();
			List<Object> parameters = new ArrayList<>();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (isFirstField) {
					sqlStr.append(" AND ");
				} else {
					isFirstField = true;
				}
				if (fields[i].getType().getSimpleName().equals("String")) {
					sqlStr.append("(? is null OR ? = \"\" OR ");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" LIKE ?)");
					parameters.add(fields[i].get(t));
					parameters.add(fields[i].get(t));
					parameters.add("%" + fields[i].get(t) + "%");
				} else if (fields[i].getType().getSimpleName().equals("int")
						|| fields[i].getType().getSimpleName().equals("long")
						|| fields[i].getType().getSimpleName().equals("double")
						|| fields[i].getType().getSimpleName().equals("float")) {
					sqlStr.append("(? = 0 OR ");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" = ?)");
					parameters.add(fields[i].get(t));
					parameters.add(fields[i].get(t));
				} else {
					sqlStr.append("( ? is null OR ");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" = ?)");
					parameters.add(fields[i].get(t));
					parameters.add(fields[i].get(t));
				}

			}
			return jdbc.query(sqlStr.toString(), parameters.toArray(new Object[parameters.size()]),
					new BeanPropertyRowMapper<>(clazz));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * String value will be compare by "=" instead of "LIKE"
	 */
	public List<T> find(T t) {
		try {
			clazz = getConcreteClass();
			Table table = clazz.getAnnotation(Table.class);

			// Build SQL Query
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("SELECT * FROM ");
			sqlStr.append(table.name());
			sqlStr.append(" WHERE ");
			boolean isFirstField = false;
			Field[] fields = clazz.getDeclaredFields();
			List<Object> parameters = new ArrayList<>();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (isFirstField) {
					sqlStr.append(" AND ");
				} else {
					isFirstField = true;
				}
				if (fields[i].getType().getSimpleName().equals("String")) {
					sqlStr.append("(? is null OR ");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" = ?)");
					parameters.add(fields[i].get(t));
					parameters.add(fields[i].get(t));
				} else if (fields[i].getType().getSimpleName().equals("int")
						|| fields[i].getType().getSimpleName().equals("long")
						|| fields[i].getType().getSimpleName().equals("double")
						|| fields[i].getType().getSimpleName().equals("float")) {
					sqlStr.append("(? = 0 OR ");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" = ?)");
					parameters.add(fields[i].get(t));
					parameters.add(fields[i].get(t));
				} else {
					sqlStr.append("( ? is null OR ");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" = ?)");
					parameters.add(fields[i].get(t));
					parameters.add(fields[i].get(t));
				}
			}
			return jdbc.query(sqlStr.toString(), parameters.toArray(new Object[parameters.size()]),
					new BeanPropertyRowMapper<>(clazz));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<T> find(T t, int page, EnumConstant.SQLOrderEnum orderByType, String orderField) {
		try {
			clazz = getConcreteClass();
			Table table = clazz.getAnnotation(Table.class);

			// Build SQL Query
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("SELECT * FROM ");
			sqlStr.append(table.name());
			sqlStr.append(" WHERE ");
			boolean isFirstField = false;
			Field[] fields = clazz.getDeclaredFields();
			List<Object> parameters = new ArrayList<>();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (isFirstField) {
					sqlStr.append(" AND ");
				} else {
					isFirstField = true;
				}
				if (fields[i].getType().getSimpleName().equals("String")) {
					sqlStr.append("(? is null OR ? = \"\" OR ");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" LIKE ?)");
					parameters.add(fields[i].get(t));
					parameters.add(fields[i].get(t));
					parameters.add("%" + fields[i].get(t) + "%");
				} else if (fields[i].getType().getSimpleName().equals("int")
						|| fields[i].getType().getSimpleName().equals("long")
						|| fields[i].getType().getSimpleName().equals("double")
						|| fields[i].getType().getSimpleName().equals("float")) {
					sqlStr.append("(? = 0 OR ");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" = ?)");
					parameters.add(fields[i].get(t));
					parameters.add(fields[i].get(t));
				} else {
					sqlStr.append("(");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" = ?)");
					parameters.add(fields[i].get(t));
				}

			}
			sqlStr.append(" ORDER BY ").append(orderField).append(" ").append(orderByType.toString());
			sqlStr.append(" LIMIT ").append((page - 1) * Constant.SQL.LIMIT_PER_PAGE).append(",")
					.append(Constant.SQL.LIMIT_PER_PAGE);
			return jdbc.query(sqlStr.toString(), parameters.toArray(new Object[parameters.size()]),
					new BeanPropertyRowMapper<>(clazz));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int findCountPage(T t) {
		try {
			clazz = getConcreteClass();
			Table table = clazz.getAnnotation(Table.class);

			// Build SQL Query
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("SELECT COUNT(*) FROM ");
			sqlStr.append(table.name());
			sqlStr.append(" WHERE ");
			boolean isFirstField = false;
			Field[] fields = clazz.getDeclaredFields();
			List<Object> parameters = new ArrayList<>();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (isFirstField) {
					sqlStr.append(" AND ");
				} else {
					isFirstField = true;
				}
				if (fields[i].getType().getSimpleName().equals("String")) {
					sqlStr.append("(? is null OR ? = \"\" OR ");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" LIKE ?)");
					parameters.add(fields[i].get(t));
					parameters.add(fields[i].get(t));
					parameters.add("%" + fields[i].get(t) + "%");
				} else if (fields[i].getType().getSimpleName().equals("int")
						|| fields[i].getType().getSimpleName().equals("long")
						|| fields[i].getType().getSimpleName().equals("double")
						|| fields[i].getType().getSimpleName().equals("float")) {
					sqlStr.append("(? = 0 OR ");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" = ?)");
					parameters.add(fields[i].get(t));
					parameters.add(fields[i].get(t));
				} else {
					sqlStr.append("(");
					sqlStr.append(fields[i].isAnnotationPresent(Key.class) ? fields[i].getAnnotation(Key.class).name()
							: fields[i].getAnnotation(Column.class).name());
					sqlStr.append(" = ?)");
					parameters.add(fields[i].get(t));
				}

			}
			int totalRecords = jdbc.queryForObject(sqlStr.toString(), parameters.toArray(new Object[parameters.size()]),
					Integer.class);
			return totalRecords % Constant.SQL.LIMIT_PER_PAGE > 0 ? totalRecords / Constant.SQL.LIMIT_PER_PAGE + 1
					: totalRecords / Constant.SQL.LIMIT_PER_PAGE;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public T get(int id) {
		clazz = getConcreteClass();
		Key key = null;
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(Key.class)) {
				key = field.getAnnotation(Key.class);
				break;
			}
		}
		Table table = clazz.getAnnotation(Table.class);
		try {
			return jdbc.queryForObject(String.format("SELECT * FROM %s WHERE %s = ?", table.name(), key.name()),
					new BeanPropertyRowMapper<>(clazz), id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

	}

	public boolean create(T t) {
		try {
			clazz = getConcreteClass();
			Table table = clazz.getAnnotation(Table.class);

			// Build SQL Query
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("INSERT INTO ");
			sqlStr.append(table.name()).append("(");

			StringBuffer valueSqlStr = new StringBuffer();
			valueSqlStr.append(" VALUE(");
			Field[] baseFields = clazz.getDeclaredFields();
			Field[] superFields = clazz.getSuperclass().getDeclaredFields();
			Field[] fields = Stream.of(baseFields, superFields).flatMap(Stream::of).toArray(Field[]::new);
			List<Object> parameters = new ArrayList<>();
			boolean isFirstField = false;
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (!fields[i].isAnnotationPresent(Key.class)) {
					if (!isFirstField) {
						sqlStr.append(fields[i].getAnnotation(Column.class).name());
						valueSqlStr.append("?");
						isFirstField = true;
					} else {
						sqlStr.append(",").append(fields[i].getAnnotation(Column.class).name());
						valueSqlStr.append(",").append("?");
					}
					parameters.add(fields[i].get(t));
				} else if (!fields[i].getAnnotation(Key.class).isAI()) {
					if (!isFirstField) {
						sqlStr.append(fields[i].getAnnotation(Key.class).name());
						valueSqlStr.append("?");
						isFirstField = true;
					} else {
						sqlStr.append(",").append(fields[i].getAnnotation(Key.class).name());
						valueSqlStr.append(",").append("?");
					}
					parameters.add(fields[i].get(t));
				}

			}
			valueSqlStr.append(")");
			sqlStr.append(")");
			sqlStr.append(valueSqlStr);
			jdbc.update(sqlStr.toString(), parameters.toArray(new Object[parameters.size()]));
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void create(List<T> list) {
		try {
			clazz = getConcreteClass();
			Table table = clazz.getAnnotation(Table.class);

			// Build SQL Query
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("INSERT INTO ");
			sqlStr.append(table.name()).append("(");

			StringBuffer valueSqlStr = new StringBuffer();
			valueSqlStr.append(" VALUE(");
			Field[] baseFields = clazz.getDeclaredFields();
			Field[] superFields = clazz.getSuperclass().getDeclaredFields();
			Field[] fields = Stream.of(baseFields, superFields).flatMap(Stream::of).toArray(Field[]::new);

			boolean isFirstField = false;
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (!fields[i].isAnnotationPresent(Key.class)) {
					if (!isFirstField) {
						sqlStr.append(fields[i].getAnnotation(Column.class).name());
						valueSqlStr.append("?");
						isFirstField = true;
					} else {
						sqlStr.append(",").append(fields[i].getAnnotation(Column.class).name());
						valueSqlStr.append(",").append("?");
					}
				} else if (!fields[i].getAnnotation(Key.class).isAI()) {
					if (!isFirstField) {
						sqlStr.append(fields[i].getAnnotation(Key.class).name());
						valueSqlStr.append("?");
						isFirstField = true;
					} else {
						sqlStr.append(",").append(fields[i].getAnnotation(Key.class).name());
						valueSqlStr.append(",").append("?");
					}
				}
			}
			valueSqlStr.append(")");
			sqlStr.append(")");
			sqlStr.append(valueSqlStr);

			jdbc.batchUpdate(sqlStr.toString(), new BatchPreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps, int i) throws SQLException {
					int start = 0;
					int pos = 1;
					if (fields[0].getAnnotation(Key.class).isAI()) {
						start++;
					}
					for (int j = start; j < fields.length; j++) {
						try {
							ps.setObject(pos, fields[j].get(list.get(i)));
							pos++;
						} catch (IllegalArgumentException | IllegalAccessException e) {
							e.printStackTrace();
						}
					}
				}

				@Override
				public int getBatchSize() {
					return list.size();
				}
			});

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public boolean update(T t) {
		try {
			clazz = getConcreteClass();
			Table table = clazz.getAnnotation(Table.class);

			// Build SQL Query
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("UPDATE ");
			sqlStr.append(table.name());
			sqlStr.append(" SET ");

			StringBuffer whereSqlStr = new StringBuffer();
			whereSqlStr.append(" WHERE ");
			Field[] baseFields = clazz.getDeclaredFields();
			Field[] superFields = clazz.getSuperclass().getDeclaredFields();
			Field[] fields = Stream.of(baseFields, superFields).flatMap(Stream::of).toArray(Field[]::new);
			List<Object> parameters = new ArrayList<>();
			Object id = null;
			boolean isFirstField = false;
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (fields[i].isAnnotationPresent(Key.class)) {
					id = fields[i].get(t);
					whereSqlStr.append(fields[i].getAnnotation(Key.class).name()).append(" = ?");
				} else if (!fields[i].getAnnotation(Column.class).ignoreUpdate()) {
					if (!isFirstField) {
						sqlStr.append(fields[i].getAnnotation(Column.class).name()).append(" = ?");
						isFirstField = true;
					} else {
						sqlStr.append(",").append(fields[i].getAnnotation(Column.class).name()).append(" = ?");
					}
					parameters.add(fields[i].get(t));
				}
			}
			parameters.add(id);
			sqlStr.append(whereSqlStr);
			jdbc.update(sqlStr.toString(), parameters.toArray(new Object[parameters.size()]));
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean delete(T t) {
		try {
			clazz = getConcreteClass();
			Table table = clazz.getAnnotation(Table.class);

			// Build SQL Query
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("DELETE FROM ");
			sqlStr.append(table.name());
			sqlStr.append(" WHERE ");
			Field[] fields = clazz.getDeclaredFields();
			List<Object> parameters = new ArrayList<>();
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (fields[i].isAnnotationPresent(Key.class)) {
					sqlStr.append(fields[i].getAnnotation(Key.class).name()).append(" = ?");
					parameters.add(fields[i].get(t));
					break;
				}
			}
			jdbc.update(sqlStr.toString(), parameters.toArray(new Object[parameters.size()]));
			return true;
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return false;
		}
	}

	public long createReturnID(T t) {
		try {
			clazz = getConcreteClass();
			Table table = clazz.getAnnotation(Table.class);
			KeyHolder keyHolder = new GeneratedKeyHolder();
			// Build SQL Query
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append("INSERT INTO ");
			sqlStr.append(table.name()).append("(");

			StringBuffer valueSqlStr = new StringBuffer();
			valueSqlStr.append(" VALUE(");
			Field[] baseFields = clazz.getDeclaredFields();
			Field[] superFields = clazz.getSuperclass().getDeclaredFields();
			Field[] fields = Stream.of(baseFields, superFields).flatMap(Stream::of).toArray(Field[]::new);
			List<Object> parameters = new ArrayList<>();
			boolean isFirstField = false;
			for (int i = 0; i < fields.length; i++) {
				fields[i].setAccessible(true);
				if (!fields[i].isAnnotationPresent(Key.class)) {
					if (!isFirstField) {
						sqlStr.append(fields[i].getAnnotation(Column.class).name());
						valueSqlStr.append("?");
						isFirstField = true;
					} else {
						sqlStr.append(",").append(fields[i].getAnnotation(Column.class).name());
						valueSqlStr.append(",").append("?");
					}
					parameters.add(fields[i].get(t));
				} else if (!fields[i].getAnnotation(Key.class).isAI()) {
					if (!isFirstField) {
						sqlStr.append(fields[i].getAnnotation(Key.class).name());
						valueSqlStr.append("?");
						isFirstField = true;
					} else {
						sqlStr.append(",").append(fields[i].getAnnotation(Key.class).name());
						valueSqlStr.append(",").append("?");
					}
					parameters.add(fields[i].get(t));
				}

			}
			valueSqlStr.append(")");
			sqlStr.append(")");
			sqlStr.append(valueSqlStr);

			jdbc.update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					PreparedStatement ps = con.prepareStatement(sqlStr.toString(), Statement.RETURN_GENERATED_KEYS);
					for (int i = 0; i < parameters.size(); i++) {
						ps.setObject(i + 1, parameters.get(i));
					}

					return ps;
				}
			}, keyHolder);

			return keyHolder.getKey().longValue();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			e.printStackTrace();
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	private Class<T> getConcreteClass() {
		Type type = getClass().getGenericSuperclass();
		while (!(type instanceof ParameterizedType) || ((ParameterizedType) type).getRawType() != BaseDAO.class) {
			if (type instanceof ParameterizedType) {
				type = ((Class<?>) ((ParameterizedType) type).getRawType()).getGenericSuperclass();
			} else {
				type = ((Class<?>) type).getGenericSuperclass();
			}
		}
		return (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
	}
}
