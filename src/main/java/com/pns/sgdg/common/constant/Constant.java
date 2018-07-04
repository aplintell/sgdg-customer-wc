package com.pns.sgdg.common.constant;

public class Constant {

	public final static String SUCCESS = "SUCCESS";

	public static class SQL {
		public final static int LIMIT_PER_PAGE = 10;
	}

	public static class Cookie {
		public final static String CUSTOMER_COOKIE = "ccsid";
		public final static String USER_COOKIE = "ucsid";
	}

	public static class Permission {
		public final static int SEARCH_TOPIC = 1;
	}

	public static class Error {
		public final static String LOGIN_ID_NOT_EXIST = "LOGIN_ID_NOT_EXIST";
		public final static String PASSWORD_INCORRECT = "PASSWORD_INCORRECT";
	}

	public static class Role {
		public final static int CUSTOMER = 0;
	}
}
