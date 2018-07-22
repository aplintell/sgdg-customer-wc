package com.pns.sgdg.webservice;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pns.sgdg.annotation.CheckPermission;
import com.pns.sgdg.common.constant.Constant;
import com.pns.sgdg.common.constant.EnumConstant;
import com.pns.sgdg.dao.BrandDAO;
import com.pns.sgdg.entity.Brand;
import com.pns.sgdg.security.UserSession;

@RestController
@RequestMapping("/brand")
public class BrandWS {

	@Autowired
	BrandDAO brandDAO;
	@Autowired
	UserSession userSession;

	@RequestMapping("/search")
	@CheckPermission(permission = { Constant.Permission.VIEW_BRAND_MANAGEMENT })
	public Object search(@RequestParam("name") String name, @RequestParam("page") int page) {
		Map<String, Object> result = new HashMap();
		Brand brand = new Brand();
		brand.setName(name);
		List<Brand> brands = brandDAO.find(brand, page, EnumConstant.SQLOrderEnum.ASC, "name");
		int totalRecord = brandDAO.findCount(brand);
		result.put("brands", brands);
		result.put("totalPage",
				totalRecord % Constant.SQL.LIMIT_PER_PAGE > 0 ? totalRecord / Constant.SQL.LIMIT_PER_PAGE + 1
						: totalRecord / Constant.SQL.LIMIT_PER_PAGE);
		result.put("totalRecord", totalRecord);
		return result;
	}

	@RequestMapping("/save")
	@CheckPermission(permission = { Constant.Permission.SAVE_BRAND_MANAGEMENT })
	public Object saveTopic(@RequestParam("image") MultipartFile image, @RequestParam("brandId") int brandId,
			@RequestParam("name") String name, @RequestParam("priority") int priority,
			@RequestParam("status") String status) throws NoSuchAlgorithmException, IOException {
		Brand brand = new Brand(brandId, name, image.getBytes(), priority, status);
		brand.setUpdatedBy(userSession.getLoginId());
		if (brandId == 0) {
			brand.setCreatedBy(userSession.getLoginId());
			return brandDAO.create(brand);
		} else {
			return brandDAO.update(brand);
		}

	}

	@RequestMapping("/{id}")
	@CheckPermission(permission = { Constant.Permission.SAVE_BRAND_MANAGEMENT })
	public Object getToptic(@PathVariable(value = "id") int id) {
		Brand brand = brandDAO.get(id);
		return brand;
	}

	@RequestMapping("/delete/{id}")
	@CheckPermission(permission = { Constant.Permission.DELETE_CATEGORY_MANAGEMENT })
	public Object search(@PathVariable("id") int id) {
		Brand brand = new Brand();
		brand.setBrandId(id);
		brand.setUpdatedBy(userSession.getLoginId());
		return brandDAO.delete(brand);
	}

}
