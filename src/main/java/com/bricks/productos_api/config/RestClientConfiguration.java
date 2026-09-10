package com.bricks.productos_api.config;


import org.springframework.context.annotation.*;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Bean
    public RestClient getRestClient(){
        return RestClient.create();
    }

}
