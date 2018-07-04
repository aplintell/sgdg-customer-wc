package com.pns.sgdg.utility;

import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.apache.commons.io.IOUtils;

public class ResourceUtil {

	private static final ConcurrentMap<String, String> map = new ConcurrentHashMap<>();

	public static String getResouce(String resouceName) {
		if (map.get(resouceName) != null && !map.get(resouceName).equals("")) {
			return map.get(resouceName);
		}
		InputStream ipStream = ResourceUtil.class.getResourceAsStream("/sql/" + resouceName + ".sql");
		try {
			String sql = IOUtils.toString(ipStream);
			map.put(resouceName, sql);
			return sql;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(ipStream);
		}
		return null;
	}
}
