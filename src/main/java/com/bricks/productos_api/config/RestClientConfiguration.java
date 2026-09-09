package com.bricks.productos_api.config;


import lombok.*;
import org.springframework.context.annotation.*;
import org.springframework.web.client.RestClient;

@Configuration
@Data
public class RestClientConfiguration {

    @Bean
    public RestClient getRestClient(){
        return RestClient.create();
    }

}
