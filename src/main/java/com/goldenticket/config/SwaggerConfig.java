package com.goldenticket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
 
@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(apiInfo());
        		//.addServersItem(new Server().url("/"))
        		//.scanPackages("com.myproject.controller");
        	
    }
 
    private Info apiInfo() {
        return new Info()
                .title("Golden Ticket")
                .description("Rest API 설명서")
                .version("1.0.0");
    }
}