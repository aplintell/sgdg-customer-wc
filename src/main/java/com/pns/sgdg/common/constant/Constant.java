package com.pns.sgdg.common.constant;

public class Constant {

	public final static String SUCCESS = "SUCCESS";

	public static class SQL {
		public final static int LIMIT_PER_PAGE = 10;
	}

	public static class Cookie {
		public final static String USER_COOKIE = "ccsid";
	}

	public static class Permission {
		public final static int VIEW_USER_MANAGEMENT = 1;
		public final static int SAVE_USER_MANAGEMENT = 2;
		public final static int DELETE_USER_MANAGEMENT = 3;
		public final static int CHANGE_PASSWORD_USER_MANAGEMENT = 4;

		public final static int VIEW_CUSTOMER_MANAGEMENT = 10;
		public final static int SAVE_CUSTOMER_MANAGEMENT = 11;
		public final static int DELETE_CUSTOMER_MANAGEMENT = 12;

		public final static int VIEW_CATEGORY_MANAGEMENT = 20;
		public final static int SAVE_CATEGORY_MANAGEMENT = 21;
		public final static int DELETE_CATEGORY_MANAGEMENT = 22;

		public final static int VIEW_BRAND_MANAGEMENT = 30;
		public final static int SAVE_BRAND_MANAGEMENT = 31;
		public final static int DELETE_BRAND_MANAGEMENT = 32;
	}

	public static class Error {
		public final static String LOGIN_ID_NOT_EXIST = "LOGIN_ID_NOT_EXIST";
		public final static String PASSWORD_INCORRECT = "PASSWORD_INCORRECT";
	}

	public static class Role {
		public final static int CUSTOMER = 0;
		public final static int GUEST = -1;
	}
}
