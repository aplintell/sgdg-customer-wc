package com.pns.sgdg.webservice;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pns.sgdg.annotation.CheckPermission;
import com.pns.sgdg.common.constant.Constant;
import com.pns.sgdg.dao.CustomerDAO;
import com.pns.sgdg.dao.CustomerInfoDAO;
import com.pns.sgdg.entity.Customer;
import com.pns.sgdg.entity.CustomerInfo;
import com.pns.sgdg.model.CustomerBO;
import com.pns.sgdg.model.Me;
import com.pns.sgdg.security.MD5;
import com.pns.sgdg.security.UserSession;
import com.pns.sgdg.utility.Utility;

@RestController
@RequestMapping("/customer")
public class CustomerWS {

	@Autowired
	CustomerDAO customerDAO;
	@Autowired
	CustomerInfoDAO customerInfoDAO;
	@Autowired
	UserSession userSession;

	@RequestMapping("/search")
	@CheckPermission(permission = { Constant.Permission.VIEW_CUSTOMER_MANAGEMENT })
	public Object search(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("loginId") String loginId, @RequestParam("email") String email,
			@RequestParam("page") int page) {
		return customerDAO.find(firstName, lastName, loginId, email, page);
	}

	@RequestMapping("/{id}")
	@CheckPermission(permission = { Constant.Permission.SAVE_CUSTOMER_MANAGEMENT })
	public Object getToptic(@PathVariable(value = "id") int id) {
		CustomerBO customer = customerDAO.getById(id);
		return customer;
	}

	@RequestMapping(value = "/isLoginIdExist")
	public boolean isLoginIdExist(@RequestParam("loginId") String loginId) {
		Customer customer = new Customer();
		customer.setLoginId(loginId);
		if (customerDAO.find(customer).size() == 0) {
			return false;
		}
		return true;
	}

	@RequestMapping("/save")
	@Transactional
	public Object save(@RequestBody Customer customer) throws NoSuchAlgorithmException {
		if (customer.getCustomerId() == 0) {
			encodingPassword(customer);
			long id = customerDAO.createReturnID(customer);
			CustomerInfo customerInfo = new CustomerInfo(id, "", "");
			return customerInfoDAO.create(customerInfo);
		} else {
			return customerDAO.update(customer);
		}
	}

	@ResponseBody
	@RequestMapping(value = "/me", method = RequestMethod.POST)
	public Object me() {
		if (userSession.getId() != 0 && userSession.getRoleId() == Constant.Role.CUSTOMER) {
			return customerDAO.getMe(userSession.getId());
		}
		return new Me();

	}

	@ResponseBody
	@RequestMapping("/login")
	public Object search(@RequestParam("loginId") String loginId, @RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchAlgorithmException {
		Customer customer = new Customer();
		customer.setLoginId(loginId);
		List<Customer> list = customerDAO.find(customer);
		if (list.size() == 0) {
			return Constant.Error.LOGIN_ID_NOT_EXIST;
		}
		customer = list.get(0);

		String base64Password = customer.getPassword();
		String base64Salt = customer.getSalt();
		byte[] bPassword = Utility.base64ToByte(base64Password);
		byte[] bSalt = Utility.base64ToByte(base64Salt);
		byte[] bInputPassword = Utility.getHashedPassword(password, bSalt);
		if (!Arrays.equals(bInputPassword, bPassword)) {
			return Constant.Error.PASSWORD_INCORRECT;
		}

		String encryptString = customer.getCustomerId() + "-" + customer.getLoginId() + "-" + Constant.Role.CUSTOMER
				+ "-" + new Date().getTime();
		encryptString = encryptString + "-" + MD5.getMD5(encryptString);
		encryptString = Base64.getEncoder().encodeToString(encryptString.getBytes());
		return encryptString;
	}

	private void encodingPassword(Customer customer) throws NoSuchAlgorithmException {
		byte[] salt = Utility.getHashedSalt();
		customer.setPassword(
				Utility.byteToBase64(Utility.getHashedPassword(customer.getPassword().toLowerCase(), salt)));
		customer.setSalt(Utility.byteToBase64(salt));
	}
}
