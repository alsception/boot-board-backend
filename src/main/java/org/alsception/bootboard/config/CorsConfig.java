
package org.alsception.bootboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
            /*    registry.addMapping("/**") // Allow all endpoints
                        .allowedOrigins() // Allow your frontend's origin
                        .allowedOrigins("http://localhost:4200","https://16ef-2a05-4f46-130c-cc00-5267-5350-55fb-9291.ngrok-free.app","https://e894-2a05-4f46-130c-cc00-1377-733a-356c-3ec6.ngrok-free.app") // Allow your frontend's origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow credentials (if needed)
              */  
                
                //Allow everything
                registry.addMapping("/**") // Allow all endpoints
                        .allowedOrigins("*") // Allow all origins
                        .allowedMethods("*") // Allow all HTTP methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(false); // Disable credentials (because allowing credentials with '*' origins is not allowed)
           
            }
        };
    }
}
