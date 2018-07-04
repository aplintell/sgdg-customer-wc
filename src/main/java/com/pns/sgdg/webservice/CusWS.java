package com.pns.sgdg.webservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pns.sgdg.dao.CustomerDAO;
import com.pns.sgdg.dao.TestDAO;

@RestController
@RequestMapping("/cus")
public class CusWS {

	@Autowired
	CustomerDAO customerDAO;

	@Autowired
	TestDAO testDAO;

	@RequestMapping("/getAll")
	@ResponseBody
	public Object getAllCustomer() {
		return testDAO.getAll();
		// return "You call me";
	}
}
