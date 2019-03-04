package com.mvw.medicalvisualteaching.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SP存储工具类
 */
public class SPUtil {

	private static SharedPreferences sp;
	private static SPUtil instance;
	private SPUtil() {
	}
	public static SPUtil getInstance(Context context) {

		if (instance == null) {
			instance = new SPUtil();
		}
		if (sp == null) {
			sp = context.getSharedPreferences("national_medical_phone", Context.MODE_PRIVATE);
		}
		return instance;

	}
	/* 保存数据 String int boolean  long */
	public boolean save(String key, Object value) {
		boolean result = false;
		if (value instanceof String) {
			result = sp.edit().putString(key, (String) value).commit();
		} else if (value instanceof Integer) {
			result = sp.edit().putInt(key, (Integer) value).commit();
		} else if (value instanceof Boolean) {
			result = sp.edit().putBoolean(key, (Boolean) value).commit();
		} else if (value instanceof Long) {
			result = sp.edit().putLong(key, (Long) value).commit();
		}
		return result;
	}

	public String getString(String key, String defValue) {
		return sp.getString(key, defValue);
	}

	public int getInt(String key, int defValue) {
		return sp.getInt(key, defValue);
	}

	public boolean getBoolean(String key, boolean defValue) {
		return sp.getBoolean(key, defValue);
	}
	public long getLong(String key, long defValue) {
		return sp.getLong(key, defValue);
	}

	public void remove(String key) {
		sp.edit().remove(key).commit();
	}
}
