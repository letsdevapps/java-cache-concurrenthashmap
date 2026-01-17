package com.pro.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheSimple {

	private static final Map<String, String> cache = new HashMap<>();

	public static String get(String key) {
		return cache.get(key);
	}

	public static void put(String key, String value) {
		cache.put(key, value);
	}
}