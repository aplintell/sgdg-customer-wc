package com.pns.sgdg.webservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pns.sgdg.common.constant.EnumConstant;
import com.pns.sgdg.dao.CategoryDAO;
import com.pns.sgdg.entity.Category;

@RestController
@RequestMapping("/category")
public class CategoryWS {

	@Autowired
	CategoryDAO categoryDAO;

	@RequestMapping("/getActiveCategories")
	public List<Category> getActiveCategories() {
		Category category = new Category();
		category.setStatus(EnumConstant.CategoryStatus.ACTIVE.toString());
		return categoryDAO.find(category);
	}

}
