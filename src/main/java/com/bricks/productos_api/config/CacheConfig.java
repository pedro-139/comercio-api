package com.bricks.productos_api.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

    // Con tener esta clase con estas dos anotaciones,
    // Spring Boot detecta automáticamente que debe encender la caché.

}