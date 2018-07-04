package com.pns.sgdg.utility;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 *
 * @author dung
 */
public class Utility {

	private static Pattern pattern;
	private static Matcher matcher;
	private static final String HTML_TAG_PATTERN = ".*\\<[^>]+>.*";
	private static final String NUMBERIC_PATTERN = "\\d+";
	private static final String HASH_PASSWORD = "SHA-256";
	private static final String HASH_SALT = "SHA1PRNG";
	private static final int ITERATION_NUMBER = 1;

	public static byte[] getHashedPassword(String password, byte[] salt) throws NoSuchAlgorithmException {
		try {
			MessageDigest digest = MessageDigest.getInstance(HASH_PASSWORD);
			digest.reset();
			digest.update(salt);
			byte[] input = digest.digest(password.getBytes("UTF-8"));
			for (int i = 0; i < ITERATION_NUMBER; i++) {
				digest.reset();
				input = digest.digest(input);
			}
			return input;
		} catch (UnsupportedEncodingException ex) {
			Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
		return null;
	}

	public static byte[] getHashedSalt() throws NoSuchAlgorithmException {
		try {
			// Uses a secure Random not a simple Random
			SecureRandom random = SecureRandom.getInstance(HASH_SALT);
			// Salt generation 256 bits long
			byte[] bSalt = new byte[32];
			random.nextBytes(bSalt);
			return bSalt;
		} catch (NoSuchAlgorithmException ex) {
			Logger.getLogger(Utility.class.getName()).log(Level.SEVERE, ex.getMessage(), ex);
		}
		return null;
	}

	/**
	 * From a byte[] returns a base 64 representation
	 *
	 * @param data
	 *            byte[]
	 * @return String
	 * @throws IOException
	 */
	public static String byteToBase64(byte[] data) {
		BASE64Encoder endecoder = new BASE64Encoder();
		return endecoder.encode(data);
	}

	/**
	 * From a base 64 representation, returns the corresponding byte[]
	 *
	 * @param data
	 *            String The base64 representation
	 * @return byte[]
	 * @throws IOException
	 */
	public static byte[] base64ToByte(String data) throws IOException {
		BASE64Decoder decoder = new BASE64Decoder();
		return decoder.decodeBuffer(data);
	}

	/**
	 * Validate html tag with regular expression
	 *
	 * @param tag
	 *            html tag for validation
	 * @return true valid html tag, false invalid html tag
	 */
	public static boolean validateHTMLTag(String tag) {
		if (null != tag) {
			pattern = Pattern.compile(HTML_TAG_PATTERN);
			matcher = pattern.matcher(tag);
			return matcher.matches();
		}
		return false;

	}

	public static boolean isNumeric(String s) {
		if (null == s) {
			return true;
		}
		return java.util.regex.Pattern.matches(NUMBERIC_PATTERN, s);
	}

	public static boolean checkWeekend(int day, int month, int year) {
		Calendar cal = Calendar.getInstance();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date d = sdf.parse("" + day + "/" + month + "/" + year + "");
			cal.setTime(d);
			// boolean monday = cal.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
			if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
					|| cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
				return true;
			}
		} catch (ParseException ex) {
			return true;
		}
		return false;
	}

	public static String getString(String str) {
		if (null == str) {
			return str;
		} else if ("".equals(str.trim())) {
			return null;
		}
		return str.trim();

	}

	public static String getDate(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(date);
	}

	// public static String convertDate(String date) {
	// String[] dates = date.split("-");
	// ArrayUtils.reverse(dates);
	// return dates[0] + "/" + dates[1] + "/" + dates[2];
	//
	// }
	//
	// public static String convertDateForFileName(String date) {
	// String[] dates = date.split("-");
	// ArrayUtils.reverse(dates);
	// return dates[0] + "-" + dates[1] + "-" + dates[2];
	//
	// }

	public static String getTime(String date) throws ParseException {
		String pattern = "HH:mm";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.parse(date).toString();
	}

	public static String formatStringFromDate(Date date) {
		SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/YYYY");
		return outputDateFormat.format(date);
	}

	private static char[] SPECIAL_CHARACTERS = { 'ỳ', 'ỹ', ' ', '!', '"', '#', '$', '%', '*', '+', ',', ':', '<', '=',
			'>', '?', '@', '[', '\\', ']', '^', '`', '|', '~', 'À', 'Á', 'Â', 'Ã', 'È', 'É', 'Ê', 'Ì', 'Í', 'Ò', 'Ó',
			'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â', 'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
			'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ', 'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ',
			'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ', 'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ', 'Ẽ', 'ẽ',
			'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ',
			'ồ', 'Ổ', 'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ',
			'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ', 'ữ', 'Ự', 'ự', 'Ỳ', 'Ỹ', 'ỳ', 'ỹ' };

	private static char[] REPLACEMENTS = { 'y', 'y', '-', '\0', '\0', '\0', '\0', '\0', '\0', '_', '\0', '_', '\0',
			'\0', '\0', '\0', '\0', '\0', '_', '\0', '\0', '\0', '\0', '\0', 'A', 'A', 'A', 'A', 'E', 'E', 'E', 'I',
			'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a', 'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o',
			'u', 'u', 'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u', 'A', 'a', 'A', 'a', 'A', 'a',
			'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e', 'E',
			'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o',
			'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'U',
			'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'Y', 'Y', 'y', 'y' };

	public static String toUrlFriendly(String s) {
		int maxLength = Math.min(s.length(), 236);
		char[] buffer = new char[maxLength];
		int n = 0;
		for (int i = 0; i < maxLength; i++) {
			char ch = s.charAt(i);
			buffer[n] = removeAccent(ch);
			// skip not printable characters
			if (buffer[n] > 31) {
				n++;
			}
		}
		// skip trailing slashes
		while (n > 0 && buffer[n - 1] == '/') {
			n--;
		}
		return String.valueOf(buffer, 0, n);
	}

	public static char removeAccent(char ch) {
		if (("" + ch).equals("ỳ") || ("" + ch).equals("ỹ")) {
			return 'y';
		}
		if (("" + ch).equals("ì") || ("" + ch).equals("í")) {
			return 'i';
		}
		int index = Arrays.binarySearch(SPECIAL_CHARACTERS, ch);
		if (index >= 0) {
			ch = REPLACEMENTS[index];
		}
		return ch;
	}

	public static String removeAccent(String s) {
		StringBuilder sb = new StringBuilder(s);
		for (int i = 0; i < sb.length(); i++) {
			sb.setCharAt(i, removeAccent(sb.charAt(i)));
		}
		return sb.toString();
	}

}
