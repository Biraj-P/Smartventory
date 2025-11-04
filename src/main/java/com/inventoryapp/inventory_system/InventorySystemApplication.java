package com.inventoryapp.inventory_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
public class InventorySystemApplication {

	public static void main(String[] args) {
		// Ensure JVM default timezone is a valid IANA ID for PostgreSQL
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
		SpringApplication.run(InventorySystemApplication.class, args);
	}

}
