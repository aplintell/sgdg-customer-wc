package com.pns.sgdg.webservice;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pns.sgdg.annotation.CheckPermission;
import com.pns.sgdg.common.constant.Constant;
import com.pns.sgdg.common.constant.EnumConstant;
import com.pns.sgdg.dao.CategoryDAO;
import com.pns.sgdg.entity.Category;
import com.pns.sgdg.security.UserSession;
import com.pns.sgdg.utility.Utility;

@RestController
@RequestMapping("/category")
public class CategoryWS {

	@Autowired
	CategoryDAO categoryDAO;
	@Autowired
	UserSession userSession;

	@RequestMapping("/getActiveCategories")
	public List<Category> getActiveCategories() {
		Category category = new Category();
		category.setStatus(EnumConstant.CategoryStatus.ACTIVE.toString());
		return categoryDAO.find(category);
	}

	@RequestMapping("/search")
	@CheckPermission(permission = { Constant.Permission.VIEW_CATEGORY_MANAGEMENT })
	public Object search(@RequestParam("name") String name, @RequestParam("page") int page) {
		Map<String, Object> result = new HashMap();
		Category category = new Category();
		category.setName(name);
		List<Category> categories = categoryDAO.find(category, page, EnumConstant.SQLOrderEnum.ASC, "name");
		int totalRecord = categoryDAO.findCount(category);
		result.put("categories", categories);
		result.put("totalPage",
				totalRecord % Constant.SQL.LIMIT_PER_PAGE > 0 ? totalRecord / Constant.SQL.LIMIT_PER_PAGE + 1
						: totalRecord / Constant.SQL.LIMIT_PER_PAGE);
		result.put("totalRecord", totalRecord);
		return result;
	}

	@RequestMapping("/save")
	@CheckPermission(permission = { Constant.Permission.SAVE_CATEGORY_MANAGEMENT })
	public Object saveTopic(@RequestBody Category category) throws NoSuchAlgorithmException {
		category.setUpdatedBy(userSession.getLoginId());
		category.setParsingUrl(Utility.toUrlFriendly(category.getName()));
		if (category.getCategoryId() == 0) {
			category.setCreatedBy(userSession.getLoginId());
			return categoryDAO.create(category);
		} else {
			return categoryDAO.update(category);
		}
	}

	@RequestMapping("/{id}")
	@CheckPermission(permission = { Constant.Permission.SAVE_CATEGORY_MANAGEMENT })
	public Object getToptic(@PathVariable(value = "id") int id) {
		Category category = categoryDAO.get(id);
		return category;
	}

	@RequestMapping("/delete/{id}")
	@CheckPermission(permission = { Constant.Permission.DELETE_CATEGORY_MANAGEMENT })
	public Object search(@PathVariable("id") int id) {
		Category category = new Category();
		category.setCategoryId(id);
		category.setUpdatedBy(userSession.getLoginId());
		return categoryDAO.delete(category);
	}

}
