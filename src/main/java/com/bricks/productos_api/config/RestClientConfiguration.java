package com.bricks.productos_api.config;


import org.springframework.context.annotation.*;
import org.springframework.web.client.RestClient;

/** Registra un RestClient como bean, usado por CategoriaServiceImpl para consumir la API externa.*/
@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient getRestClient(){
        return RestClient.create();
    }

}
