package org.openbpm.base.core.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 枚举的工具类
 *
 */
public class EnumUtil {
	/**
	 * 防止被实例化
	 */
	private EnumUtil() {

	}

	/**
	 * <pre>
	 * 把一个枚举类转成JSON，主要是为了方便前端直接调用(以下是jsp的用法)
	 * 前端调用例子：
	 * <%@page import="org.openbpm.sys.persistence.enums.FieldControlType"%>
	 * <%@page import="org.openbpm.base.core.util.EnumUtil"%>
	 * <script type="text/javascript">
	 * var FieldControlType = <%=EnumUtil.toJSON(FieldControlType.class)%>;
	 * </script>
	 * 系统内置的异步获取类：toolsControllerUtil.js
	 * </pre>
	 *
	 * @param enumClass
	 * @return
	 */
	public static JSONObject toJSON(Class<? extends Enum<?>> enumClass) {
		try {
			Method method = enumClass.getMethod("values");
			Enum<?>[] enums = (Enum[]) method.invoke(enumClass, null);
			JSONObject jsonObject = new JSONObject(enums.length, true);
			for (Enum<?> e : enums) {
				jsonObject.put(e.name(), toJSON(e));
			}
			return jsonObject;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static JSONArray toJSONArray(Class<? extends Enum<?>> enumClass) {
		try {
			Method method = enumClass.getMethod("values");
			Enum<?>[] enums = (Enum[]) method.invoke(enumClass, null);
			JSONArray jsonArray = new JSONArray(enums.length);
			for (Enum<?> e : enums) {
				jsonArray.add(toJSON(e));
			}
			return jsonArray;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * <pre>
	 * 把一个枚举类的路径转成json数组
	 * 注意！！如果枚举在类中间，那么路径如下：org.openbpm.base.db.model.Column$TYPE
	 * </pre>
	 *
	 * @param enumClassPath
	 *            枚举路径
	 * @return
	 * @throws Exception
	 */
	public static JSONArray toJSONArray(String enumClassPath) {
		try {
			return toJSONArray((Class<? extends Enum<?>>) Class.forName(enumClassPath));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	public static JSONObject toJSON(String enumClassPath) {
		try {
			return toJSON((Class<? extends Enum<?>>) Class.forName(enumClassPath));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * <pre>
	 * 把一个枚举实例转成JSON，主要是为了方便前端直接调用
	 * </pre>
	 *
	 * @param e
	 * @return
	 * @throws Exception
	 */
	private static JSONObject toJSON(Enum<?> e) throws Exception {
		JSONObject jsonObject = new JSONObject();
		Field[] fields = e.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);// 让私有变量也能被访问到
			// 过滤掉自身的枚举（我都想不到枚举实例为什么也算是类的字段）和枚举类必有的全部变量的ENUM$VALUES字段
			if ("ENUM$VALUES".equals(field.getName()) || field.getType().equals(e.getClass())) {
				continue;
			}
			Object obj = field.get(e);
			if (obj instanceof Enum) {// 如果类型是Enum，那刚好继续解释多一次
				jsonObject.put(field.getName(), toJSON((Enum<?>) field.get(e)));
			} else {
				jsonObject.put(field.getName(), field.get(e));
			}

		}
		return jsonObject;
	}

	public static void main(String[] args) throws Exception {
		System.out.println(EnumUtil.toJSON("org.openbpm.sys.persistence.enums.FieldControlType"));
	}
}
