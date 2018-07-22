package com.pns.sgdg.webservice;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.pns.sgdg.annotation.CheckPermission;
import com.pns.sgdg.common.constant.Constant;
import com.pns.sgdg.common.constant.EnumConstant;
import com.pns.sgdg.dao.UserDAO;
import com.pns.sgdg.entity.User;
import com.pns.sgdg.model.Me;
import com.pns.sgdg.security.MD5;
import com.pns.sgdg.security.UserSession;
import com.pns.sgdg.utility.Utility;

@RestController
@RequestMapping("/user")
public class UserWS {

	@Autowired
	UserSession userSession;
	@Autowired
	UserDAO userDAO;

	@RequestMapping("/search")
	@CheckPermission(permission = { Constant.Permission.VIEW_USER_MANAGEMENT })
	public Object search(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName,
			@RequestParam("loginId") String loginId, @RequestParam("email") String email,
			@RequestParam("page") int page) {
		Map<String, Object> result = new HashMap<>();
		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setLoginId(loginId);
		user.setEmail(email);
		List<User> users = userDAO.find(user, page, EnumConstant.SQLOrderEnum.ASC, "first_name");
		int totalRecord = userDAO.findCount(user);
		result.put("users", users);
		result.put("totalPage",
				totalRecord % Constant.SQL.LIMIT_PER_PAGE > 0 ? totalRecord / Constant.SQL.LIMIT_PER_PAGE + 1
						: totalRecord / Constant.SQL.LIMIT_PER_PAGE);
		result.put("totalRecord", totalRecord);
		return result;
	}

	@RequestMapping("/save")
	@CheckPermission(permission = { Constant.Permission.SAVE_USER_MANAGEMENT })
	public Object saveTopic(@RequestBody User user) throws NoSuchAlgorithmException {
		user.setUpdatedBy(userSession.getLoginId());
		if (user.getUserId() == 0) {
			user.setCreatedBy(userSession.getLoginId());
			user.setPassword(user.getLoginId());
			encodingPassword(user);
			return userDAO.create(user);
		} else {
			return userDAO.update(user);
		}
	}

	@RequestMapping("/{id}")
	@CheckPermission(permission = { Constant.Permission.SAVE_USER_MANAGEMENT })
	public Object getToptic(@PathVariable(value = "id") int id) {
		User user = userDAO.get(id);
		return user;
	}

	@RequestMapping("/delete/{id}")
	@CheckPermission(permission = { Constant.Permission.DELETE_USER_MANAGEMENT })
	public Object search(@PathVariable("id") int id) {
		User user = new User();
		user.setUserId(id);
		user.setUpdatedBy(userSession.getLoginId());
		return userDAO.delete(user);
	}

	@ResponseBody
	@RequestMapping(value = "/me", method = RequestMethod.POST)
	public Object me() {
		if (userSession.getId() != 0 && userSession.getRoleId() != Constant.Role.CUSTOMER) {
			return userDAO.getMe(userSession.getId());
		}
		return new Me();

	}

	@ResponseBody
	@RequestMapping("/login")
	public Object search(@RequestParam("loginId") String loginId, @RequestParam("password") String password,
			HttpServletRequest request, HttpServletResponse response) throws IOException, NoSuchAlgorithmException {
		User user = new User();
		user.setLoginId(loginId);
		List<User> list = userDAO.find(user);
		if (list.size() == 0) {
			return Constant.Error.LOGIN_ID_NOT_EXIST;
		}
		user = list.get(0);

		String base64Password = user.getPassword();
		String base64Salt = user.getSalt();
		byte[] bPassword = Utility.base64ToByte(base64Password);
		byte[] bSalt = Utility.base64ToByte(base64Salt);
		byte[] bInputPassword = Utility.getHashedPassword(password, bSalt);
		if (!Arrays.equals(bInputPassword, bPassword)) {
			return Constant.Error.PASSWORD_INCORRECT;
		}

		String encryptString = user.getUserId() + "-" + user.getLoginId() + "-" + user.getRoleId() + "-"
				+ new Date().getTime();
		encryptString = encryptString + "-" + MD5.getMD5(encryptString);
		encryptString = Base64.getEncoder().encodeToString(encryptString.getBytes());
		return encryptString;
	}

	private void encodingPassword(User user) throws NoSuchAlgorithmException {
		byte[] salt = Utility.getHashedSalt();
		user.setPassword(Utility.byteToBase64(Utility.getHashedPassword(user.getPassword().toLowerCase(), salt)));
		user.setSalt(Utility.byteToBase64(salt));
	}

}
