package com.pro.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CacheConcurrent {

	private static final ConcurrentMap<String, String> cache = new ConcurrentHashMap<>();

	public static String get(String key) {
		return cache.get(key);
	}

	public static void put(String key, String value) {
		cache.put(key, value);
	}
}