package com.pro.cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheFinal {

	private static class Entry {
		final String value;
		final long expiresAt;

		Entry(String value, long ttlMillis) {
			this.value = value;
			this.expiresAt = System.currentTimeMillis() + ttlMillis;
		}

		boolean isExpired() {
			return System.currentTimeMillis() > expiresAt;
		}
	}

	private final ConcurrentHashMap<String, Entry> cache = new ConcurrentHashMap<>();
	private final ScheduledExecutorService cleaner;

	public CacheFinal(long cleanIntervalMillis) {
		cleaner = Executors.newSingleThreadScheduledExecutor(r -> {
			Thread t = new Thread(r);
			t.setDaemon(true); // não bloqueia shutdown da JVM
			t.setName("CacheFinal-Cleaner");
			return t;
		});
		cleaner.scheduleAtFixedRate(this::evictExpired, cleanIntervalMillis, cleanIntervalMillis,
				TimeUnit.MILLISECONDS);
	}

	public void put(String key, String value, long ttlMillis) {
		cache.put(key, new Entry(value, ttlMillis));
	}

	public String get(String key) {
		return cache.computeIfPresent(key, (k, entry) -> {
			if (entry.isExpired())
				return null; // remove atômico
			return entry;
		}) != null ? cache.get(key).value : null;
	}

	private void evictExpired() {
		cache.entrySet().removeIf(e -> e.getValue().isExpired());
	}

	public void shutdown() {
		cleaner.shutdownNow();
	}
}
