package com.wonders.util;

import java.lang.reflect.Field;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {
	/**
	 * this is parsejson method.
	 * 
	 * @param bean
	 * @param json
	 * @return
	 */
	public static boolean parse(Object bean, String json) {
		Field[] fields = bean.getClass().getDeclaredFields();

		if (fields.length == 0) {
			return false;
		}

		try {
			JSONObject jsonObject = new JSONObject(json);
			stringToJsonBean(jsonObject, fields, bean);

		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * voluation
	 * 
	 * @param jsonObject
	 * @param fields
	 * @param bean
	 */
	private static void stringToJsonBean(JSONObject jsonObject, Field[] fields,
			Object bean) {
		for (Field field : fields) {
			try {
				if (field.getType() == Integer.TYPE) {
					field.setAccessible(true);
					field.setInt(bean, jsonObject.getInt(field.getName()));
				} else if (field.getType() == Boolean.TYPE) {
					field.setAccessible(true);
					field.setBoolean(bean,
							jsonObject.getBoolean(field.getName()));
				} else if (field.getType() == Long.TYPE) {
					field.setAccessible(true);
					field.setLong(bean, jsonObject.getLong(field.getName()));
				} else {
					field.setAccessible(true);
					field.set(bean, jsonObject.get(field.getName()));
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * string to json
	 * 
	 * @param json
	 * @return
	 * @throws IllegalArgumentException
	 * @throws JSONException
	 * @throws IllegalAccessException
	 */
	public static JSONObject packaging(Object json)
			throws IllegalArgumentException, JSONException,
			IllegalAccessException {
		Field[] fields = json.getClass().getDeclaredFields();
		JSONObject jsonObject = new JSONObject();

		for (Field field : fields) {
			field.setAccessible(true);
			jsonObject.put(field.getName(), field.get(json));
		}

		return jsonObject;
	}

	public static String getResult(String json) {
		String result = null;

		try {
			JSONObject jsonObject = new JSONObject(json);
			result = jsonObject.getString("result");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (result == null) {
			return new JSONObject().toString();
		}
		return result;
	}
}
