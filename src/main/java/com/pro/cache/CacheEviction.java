package com.pro.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheEviction {

	private static final ConcurrentHashMap<String, CacheEntry> cache = new ConcurrentHashMap<>();

	private static final ScheduledExecutorService cleaner = Executors.newSingleThreadScheduledExecutor();

	static {
		cleaner.scheduleAtFixedRate(() -> {
			cache.entrySet().removeIf(e -> e.getValue().isExpired());
		}, 1, 1, TimeUnit.SECONDS);
	}

	static class CacheEntry {
		String value;
		long expiresAt;

		CacheEntry(String value, long ttlMillis) {
			this.value = value;
			this.expiresAt = System.currentTimeMillis() + ttlMillis;
		}

		boolean isExpired() {
			return System.currentTimeMillis() > expiresAt;
		}
	}

	public static void put(String key, String value, long ttlMillis) {
		cache.put(key, new CacheEntry(value, ttlMillis));
	}

	public static String get(String key) {
		CacheEntry entry = cache.get(key);
		if (entry == null || entry.isExpired()) {
			cache.remove(key);
			return null;
		}
		return entry.value;
	}
}