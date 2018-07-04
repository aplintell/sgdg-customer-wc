package com.pns.sgdg.security;

import java.util.Base64;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import com.pns.sgdg.common.constant.Constant;

public class CookieUtil {

	private static final int USER_ID_POS = 0;
	private static final int USER_LOGIN_ID_POS = 1;
	private static final int ROLE_ID_POS = 2;
	private static final int DATE_TIME_POS = 3;
	private static final int MD5_POS = 4;
	private static final String SEPERATE_SYMBOL = "-";

	public static CookieData getUserCookie(HttpServletRequest request) {
		try {
			String encryptString = "";
			Cookie cookies[] = request.getCookies();
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals(Constant.Cookie.CUSTOMER_COOKIE)) {
						encryptString = cookie.getValue();
					}
				}
				byte[] decrypt = Base64.getDecoder().decode(encryptString);
				String decryptString = new String(decrypt);
				String md5 = MD5.getMD5(decryptString.split(SEPERATE_SYMBOL)[USER_ID_POS] + SEPERATE_SYMBOL
						+ decryptString.split(SEPERATE_SYMBOL)[USER_LOGIN_ID_POS] + SEPERATE_SYMBOL
						+ decryptString.split(SEPERATE_SYMBOL)[ROLE_ID_POS] + SEPERATE_SYMBOL
						+ decryptString.split(SEPERATE_SYMBOL)[DATE_TIME_POS]);
				if (md5.equals(decryptString.split(SEPERATE_SYMBOL)[MD5_POS])) {
					return new CookieData(Long.parseLong(decryptString.split(SEPERATE_SYMBOL)[USER_ID_POS]),
							decryptString.split(SEPERATE_SYMBOL)[USER_LOGIN_ID_POS],
							Integer.parseInt(decryptString.split(SEPERATE_SYMBOL)[ROLE_ID_POS]));
				}
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static class CookieData {

		private long id;
		private String loginId;
		private int roleId;

		public CookieData() {
		}

		public CookieData(long id, String loginId, int roleId) {
			this.id = id;
			this.loginId = loginId;
			this.roleId = roleId;
		}

		public long getId() {
			return id;
		}

		public void setId(long id) {
			this.id = id;
		}

		public String getLoginId() {
			return loginId;
		}

		public void setLoginId(String loginId) {
			this.loginId = loginId;
		}

		public int getRoleId() {
			return roleId;
		}

		public void setRoleId(int roleId) {
			this.roleId = roleId;
		}

	}
}
