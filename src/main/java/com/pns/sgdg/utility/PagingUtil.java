package com.pns.sgdg.utility;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.pns.sgdg.common.constant.Constant;
import com.pns.sgdg.model.SearchPagingResult;

public class PagingUtil {

	public static SearchPagingResult query(JdbcTemplate jdbc, Class modelClass, String searchSQL, String countSQL,
			List<Object> params, int page) {
		try {

			int startLimit = (page - 1) * Constant.SQL.LIMIT_PER_PAGE;
			int totalResult = jdbc.queryForObject(ResourceUtil.getResouce(countSQL), params.toArray(), Integer.class);
			if (startLimit >= totalResult && page > 1) {
				startLimit = (page - 2) * Constant.SQL.LIMIT_PER_PAGE;
				page--;
			}

			List<Object> searchByPageParams = new ArrayList<>(params);
			searchByPageParams.add(startLimit);
			searchByPageParams.add(Constant.SQL.LIMIT_PER_PAGE);

			List<?> searchList = jdbc.query(ResourceUtil.getResouce(searchSQL), new BeanPropertyRowMapper(modelClass),
					searchByPageParams.toArray());

			return new SearchPagingResult(searchList, totalResult,
					totalResult % Constant.SQL.LIMIT_PER_PAGE > 0 ? totalResult / Constant.SQL.LIMIT_PER_PAGE + 1
							: totalResult / Constant.SQL.LIMIT_PER_PAGE);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} catch (RuntimeException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
