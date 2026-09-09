package com.bricks.productos_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ProductosApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductosApiApplication.class, args);
	}

}
