package com.wcpdoc.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Hibernate工具类
 * 
 * @author zhanghc 2015-7-21下午10:20:25
 */
public class HibernateUtil {
	/**
	 * 格式化时间</br>
	 * 
	 * v1.0 zhanghc 2015-3-30下午02:29:10
	 * 
	 * @param list
	 * @param params {"CREATETIME", "yyyy-MM-dd", "UPDATETIME", "yyyy-MM-dd HH:mm:ss"}
	 * 
	 * void
	 */
	public static void formatDate(List<Map<String, Object>> list, String... params) {
		SimpleDateFormat format = new SimpleDateFormat();

		for (Map<String, Object> map : list) {
			for (int i = 0; i < params.length; i++) {
				String key = params[i];
				String pattern = params[++i];
				Date value = (Date) map.get(key);
				if (value != null) {
					format.applyPattern(pattern);
					map.put(key, format.format(value));
				}
			}
		}
	}
}
