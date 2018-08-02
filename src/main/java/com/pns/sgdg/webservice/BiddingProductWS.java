package com.pns.sgdg.webservice;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pns.sgdg.annotation.DBConfig;
import com.pns.sgdg.dao.BiddingProductDAO;
import com.pns.sgdg.dao.CategoryDAO;
import com.pns.sgdg.dao.ImageDAO;
import com.pns.sgdg.entity.BiddingProduct;
import com.pns.sgdg.entity.BiddingProductImage;
import com.pns.sgdg.entity.Category;
import com.pns.sgdg.security.UserSession;
import com.pns.sgdg.utility.Utility;

@RestController
@RequestMapping("/bidding-product")
public class BiddingProductWS {

	@DBConfig(value = "bidding.product.image.url")
	String imageUrl;
	@DBConfig(value = "bidding.product.upload.image.dir")
	String imageUploadLocation;

	@Autowired
	UserSession userSession;
	@Autowired
	ImageDAO imageDAO;
	@Autowired
	BiddingProductDAO biddingProductDAO;
	@Autowired
	CategoryDAO categoryDAO;

	@RequestMapping("/upload")
	public Object upload(@RequestParam("file[]") MultipartFile[] file) throws IllegalStateException, IOException {
		int count = 0;
		String fileName;
		List<String> fileNames = new ArrayList<>();
		BiddingProductImage image;
		List<BiddingProductImage> images = new ArrayList<>();
		String extension = "";
		int i;

		for (MultipartFile multipartFile : file) {
			i = multipartFile.getOriginalFilename().lastIndexOf('.');
			if (i > 0) {
				extension = multipartFile.getOriginalFilename().substring(i);
			}

			fileName = "" + count + userSession.getId() + new Date().getTime() + extension;

			fileNames.add(fileName);

			File convFile = new File(imageUploadLocation + fileName);
			multipartFile.transferTo(convFile);
			count++;
			image = new BiddingProductImage();
			image.setName(fileName);
			images.add(image);
		}
		imageDAO.create(images);
		return fileNames;
	}

	@ResponseBody
	@RequestMapping("/save")
	public Object save(@RequestParam("name") String name, @RequestParam("initialPrice") double initialPrice,
			@RequestParam("priceStep") double priceStep, @RequestParam("description") String description,
			@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate,
			@RequestParam("imageNames") String[] imageNames) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
		BiddingProduct biddingProduct = new BiddingProduct(name, Utility.toUrlFriendly(name), initialPrice, priceStep,
				description, "", "", formatter.parse(startDate), formatter.parse(endDate), 0);
		long id = biddingProductDAO.createReturnID(biddingProduct);
		List<BiddingProductImage> list = new ArrayList<>();
		BiddingProductImage image;
		for (String imageName : imageNames) {
			image = new BiddingProductImage();
			image.setName(imageName);
			image = imageDAO.find(image).get(0);
			image.setBiddingProductId(id);
			list.add(image);
		}
		imageDAO.update(list);

		return "OK";
	}

	@ResponseBody
	@RequestMapping("/get")
	public Object save(@RequestParam("id") long id) {
		BiddingProduct product = biddingProductDAO.get(id);
		Category category = categoryDAO.get(product.getCategoryId());
		BiddingProductImage image = new BiddingProductImage();
		image.setBiddingProductId(id);
		List<BiddingProductImage> images = imageDAO.find(image);
		Map<String, Object> map = new HashMap<>();
		map.put("product", product);
		map.put("category", category);
		map.put("imageUrl", imageUrl);
		map.put("images", images);
		return map;
	}

}
