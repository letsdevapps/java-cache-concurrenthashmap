package com.pro;

import com.pro.cache.CacheFinal;

public class App {
	public static void main(String[] args) throws InterruptedException {
		System.out.println("----- Java Cache Concurrent HashMap | Main -----");

		CacheFinal cache = new CacheFinal(2000);

		// Coloca alguns valores com TTL diferente
		cache.put("user1", "Maria", 3000); // 3 segundos
		cache.put("user2", "Joao", 5000); // 5 segundos

		System.out.println("Inicial:");
		System.out.println("user1 = " + cache.get("user1")); // Maria
		System.out.println("user2 = " + cache.get("user2")); // Joao

		// Espera 4 segundos
		Thread.sleep(4000);

		System.out.println("\nApós 4 segundos:");
		System.out.println("user1 = " + cache.get("user1")); // null (expirou)
		System.out.println("user2 = " + cache.get("user2")); // Joao (ainda válido)

		// Limpa o cache e encerra o executor
		cache.shutdown();
	}
}
