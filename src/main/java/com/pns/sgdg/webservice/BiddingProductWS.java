package com.pns.sgdg.webservice;

import java.io.File;
import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/biddingProduct")
public class BiddingProductWS {

	@RequestMapping("/upload")
	public Object upload(@RequestParam("file[]") MultipartFile[] file) throws IllegalStateException, IOException {
		System.out.println(file.length);
		for (MultipartFile multipartFile : file) {
			String filePath = "/Users/mac24h/Desktop/workspace-sts-3.9.4.RELEASE/sgdg";
			File convFile = new File(filePath + multipartFile.getOriginalFilename());
			multipartFile.transferTo(convFile);
			System.out.println(multipartFile.getName());
		}
		return true;
	}

}
