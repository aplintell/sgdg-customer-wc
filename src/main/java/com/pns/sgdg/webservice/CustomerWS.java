package com.pns.sgdg.webservice;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.multipart.MultipartFile;

import com.pns.sgdg.annotation.CheckPermission;
import com.pns.sgdg.annotation.DBConfig;
import com.pns.sgdg.common.constant.Constant;
import com.pns.sgdg.common.constant.EnumConstant;
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

	@DBConfig(value = "customer.upload.image.dir")
	String imageUploadLocation;

	@DBConfig(value = "customer.image.url")
	String imageUrl;

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
		Map<String, Object> map = new HashMap<>();
		CustomerBO customer = customerDAO.getById(id);
		map.put("customer", customer);
		map.put("imageUrl", imageUrl);
		return map;
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

	@RequestMapping("/register")
	@Transactional
	public Object register(@RequestBody Customer customer) throws NoSuchAlgorithmException {
		if (customer.getCustomerId() == 0) {
			encodingPassword(customer);
			long id = customerDAO.createReturnID(customer);
			CustomerInfo customerInfo = new CustomerInfo();
			customerInfo.setCustomerId(id);
			return customerInfoDAO.create(customerInfo);
		} else {
			return customerDAO.update(customer);
		}
	}

	@RequestMapping("/save")
	@Transactional
	@CheckPermission(permission = { Constant.Permission.SAVE_CUSTOMER_MANAGEMENT })
	public Object save(@RequestParam(value = "avatar", required = false) MultipartFile avatar,
			@RequestParam(value = "frontIdCard", required = false) MultipartFile frontIdCard,
			@RequestParam(value = "backIdCard", required = false) MultipartFile backIdCard,
			@RequestParam("loginId") String loginId, @RequestParam("customerId") long customerId,
			@RequestParam("status") String status, @RequestParam("point") int point,
			@RequestParam("customerType") String customerType, @RequestParam("lastName") String lastName,
			@RequestParam("firstName") String firstName, @RequestParam("gender") String gender,
			@RequestParam("phone") String phone, @RequestParam("dob") String dob, @RequestParam("email") String email,
			@RequestParam("address") String address)
			throws NoSuchAlgorithmException, ParseException, IllegalStateException, IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String avatarFileName = null, frontIdCardFileName = null, backIdCardFileName = null;
		if (avatar != null) {
			avatarFileName = loginId + "-avatar"
					+ avatar.getOriginalFilename().substring(avatar.getOriginalFilename().lastIndexOf('.'));
		}

		if (frontIdCard != null) {
			frontIdCardFileName = loginId + "-font-id"
					+ frontIdCard.getOriginalFilename().substring(frontIdCard.getOriginalFilename().lastIndexOf('.'));
		}

		if (backIdCard != null) {
			backIdCardFileName = loginId + "-back-id"
					+ backIdCard.getOriginalFilename().substring(backIdCard.getOriginalFilename().lastIndexOf('.'));
		}

		CustomerInfo customerInfo = new CustomerInfo(customerId, lastName, firstName, gender, phone, email,
				sdf.parse(dob), address, avatarFileName, frontIdCardFileName, backIdCardFileName, point, customerType);

		boolean result = false;
		Customer customer = new Customer();
		customer.setLoginId(loginId);
		customer.setCustomerId(customerId);
		customer.setStatus(status);
		customer.setPassword(loginId);
		encodingPassword(customer);
		if (customerId == 0) {
			customer.setCreatedBy(userSession.getLoginId());
			long id = customerDAO.createReturnID(customer);

			customerInfo.setCustomerId(id);
			customerInfo.setCreatedBy(userSession.getLoginId());
			result = customerInfoDAO.create(customerInfo);
		} else {
			CustomerInfo existCustomerInfo = customerInfoDAO.get(customerId);
			if (avatarFileName == null) {
				customerInfo.setAvatar(existCustomerInfo.getAvatar());
			} else {
				customerInfo.setAvatar(avatarFileName);
			}

			if (frontIdCardFileName == null) {
				customerInfo.setFrontIdCard(existCustomerInfo.getFrontIdCard());
			} else {
				customerInfo.setFrontIdCard(frontIdCardFileName);
			}

			if (backIdCardFileName == null) {
				customerInfo.setBackIdCard(existCustomerInfo.getBackIdCard());
			} else {
				customerInfo.setBackIdCard(backIdCardFileName);
			}

			customerInfoDAO.update(customerInfo);
			result = customerDAO.update(customer);
		}

		if (result) {
			if (avatar != null) {
				File convFile = new File(imageUploadLocation + avatarFileName);
				avatar.transferTo(convFile);
			}

			if (frontIdCard != null) {
				File convFile = new File(imageUploadLocation + frontIdCardFileName);
				frontIdCard.transferTo(convFile);
			}

			if (backIdCard != null) {
				File convFile = new File(imageUploadLocation + backIdCardFileName);
				backIdCard.transferTo(convFile);
			}

		}

		return result;
	}

	@RequestMapping("/toggleStatus/{id}")
	@CheckPermission(permission = { Constant.Permission.SAVE_CUSTOMER_MANAGEMENT })
	public Object toggleStatus(@PathVariable("id") int id) {
		Customer customer = customerDAO.get(id);
		customer.setUpdatedBy(userSession.getLoginId());
		if (customer.getStatus().equals(EnumConstant.CustomerStatus.ACTIVE.toString())) {
			customer.setStatus(EnumConstant.CustomerStatus.INACTIVE.toString());
		} else {
			customer.setStatus(EnumConstant.CustomerStatus.ACTIVE.toString());
		}
		return customerDAO.update(customer);
	}

	@RequestMapping("/delete/{id}")
	@CheckPermission(permission = { Constant.Permission.DELETE_CUSTOMER_MANAGEMENT })
	public Object delete(@PathVariable("id") int id) {
		Customer customer = new Customer();
		customer.setCustomerId(id);
		customer.setUpdatedBy(userSession.getLoginId());
		return customerDAO.delete(customer);
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
