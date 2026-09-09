package com.bricks.productos_api.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@Data
public class RestClientConfiguration {

    @Bean
    public RestClient getRestClient(){
        return RestClient.create();
    }

}
