package com.pns.sgdg.webservice;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pns.sgdg.dao.ImageDAO;
import com.pns.sgdg.entity.Image;
import com.pns.sgdg.security.UserSession;

@RestController
@RequestMapping("/biddingProduct")
public class BiddingProductWS {

	@Autowired
	UserSession userSession;
	@Autowired
	ImageDAO imageDAO;

	@RequestMapping("/upload")
	public Object upload(@RequestParam("file[]") MultipartFile[] file) throws IllegalStateException, IOException {
		System.out.println(file.length);
		int count = 0;
		String fileName;
		List<String> fileNames = new ArrayList<>();
		Image image;
		List<Image> images = new ArrayList<>();
		String extension = "";
		int i;

		for (MultipartFile multipartFile : file) {
			i = multipartFile.getOriginalFilename().lastIndexOf('.');
			if (i > 0) {
				extension = multipartFile.getOriginalFilename().substring(i);
			}

			fileName = "" + count + userSession.getId() + new Date().getTime() + extension;

			fileNames.add(fileName);

			String filePath = "/Users/mac24h/Desktop/workspace-sts-3.9.4.RELEASE/img/";
			File convFile = new File(filePath + fileName);
			multipartFile.transferTo(convFile);
			count++;
			image = new Image();
			image.setName(fileName);
			images.add(image);
		}
		imageDAO.create(images);
		return fileNames;
	}

}
