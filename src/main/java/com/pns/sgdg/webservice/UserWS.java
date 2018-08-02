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
import org.springframework.web.bind.annotation.PathVariable;
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

	@DBConfig(value = "user.upload.image.dir")
	String imageUploadLocation;

	@DBConfig(value = "user.image.url")
	String imageUrl;

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

	// @RequestMapping("/save")
	// @CheckPermission(permission = { Constant.Permission.SAVE_USER_MANAGEMENT })
	// public Object saveTopic(@RequestBody User user) throws
	// NoSuchAlgorithmException {
	// user.setUpdatedBy(userSession.getLoginId());
	// if (user.getUserId() == 0) {
	// user.setCreatedBy(userSession.getLoginId());
	// user.setPassword(user.getLoginId());
	// encodingPassword(user);
	// return userDAO.create(user);
	// } else {
	// return userDAO.update(user);
	// }
	// }

	@RequestMapping("/save")
	@CheckPermission(permission = { Constant.Permission.SAVE_USER_MANAGEMENT })
	public Object save(@RequestParam(value = "avatar", required = false) MultipartFile avatar,
			@RequestParam(value = "frontIdCard", required = false) MultipartFile frontIdCard,
			@RequestParam(value = "backIdCard", required = false) MultipartFile backIdCard,
			@RequestParam("loginId") String loginId, @RequestParam("userId") long userId,
			@RequestParam("status") String status, @RequestParam("roleId") int roleId,
			@RequestParam("lastName") String lastName, @RequestParam("firstName") String firstName,
			@RequestParam("gender") String gender, @RequestParam("phone") String phone, @RequestParam("dob") String dob,
			@RequestParam("email") String email, @RequestParam("address") String address)
			throws NoSuchAlgorithmException, IOException, ParseException {
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
		User user = new User(userId, loginId, status, roleId, lastName, firstName, gender, phone, email, sdf.parse(dob),
				address, avatarFileName, frontIdCardFileName, backIdCardFileName);
		user.setUpdatedBy(userSession.getLoginId());
		boolean result = false;
		if (userId == 0) {
			user.setCreatedBy(userSession.getLoginId());
			user.setPassword(user.getLoginId());
			encodingPassword(user);
			result = userDAO.create(user);
		} else {
			User existUser = userDAO.get(userId);
			if (avatarFileName == null) {
				user.setAvatar(existUser.getAvatar());
			} else {
				user.setAvatar(avatarFileName);
			}

			if (frontIdCardFileName == null) {
				user.setFrontIdCard(existUser.getFrontIdCard());
			} else {
				user.setFrontIdCard(frontIdCardFileName);
			}

			if (backIdCardFileName == null) {
				user.setBackIdCard(existUser.getBackIdCard());
			} else {
				user.setBackIdCard(backIdCardFileName);
			}
			result = userDAO.update(user);
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

	@RequestMapping("/{id}")
	@CheckPermission(permission = { Constant.Permission.SAVE_USER_MANAGEMENT })
	public Object getToptic(@PathVariable(value = "id") int id) {
		Map<String, Object> map = new HashMap<>();
		User user = userDAO.get(id);
		map.put("user", user);
		map.put("imageUrl", imageUrl);
		return map;
	}

	@RequestMapping("/delete/{id}")
	@CheckPermission(permission = { Constant.Permission.DELETE_USER_MANAGEMENT })
	public Object delete(@PathVariable("id") int id) {
		User user = new User();
		user.setUserId(id);
		user.setUpdatedBy(userSession.getLoginId());
		return userDAO.delete(user);
	}

	@RequestMapping("/toggleStatus/{id}")
	@CheckPermission(permission = { Constant.Permission.SAVE_USER_MANAGEMENT })
	public Object toggleStatus(@PathVariable("id") int id) {
		User user = userDAO.get(id);
		user.setUpdatedBy(userSession.getLoginId());
		if (user.getStatus().equals(EnumConstant.UserStatus.ACTIVE.toString())) {
			user.setStatus(EnumConstant.UserStatus.INACTIVE.toString());
		} else {
			user.setStatus(EnumConstant.UserStatus.ACTIVE.toString());
		}
		return userDAO.update(user);
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
