package com.pro.cache;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class CacheTTL {

	static class Entry {
		String value;
		long expiresAt;

		Entry(String value, long ttlMillis) {
			this.value = value;
			this.expiresAt = Instant.now().toEpochMilli() + ttlMillis;
		}

		boolean isExpired() {
			return Instant.now().toEpochMilli() > expiresAt;
		}
	}

	private static final ConcurrentHashMap<String, Entry> cache = new ConcurrentHashMap<>();

	public static String get(String key) {
		Entry entry = cache.get(key);
		if (entry == null || entry.isExpired()) {
			cache.remove(key);
			return null;
		}
		return entry.value;
	}

	public static void put(String key, String value, long ttlMillis) {
		cache.put(key, new Entry(value, ttlMillis));
	}
}