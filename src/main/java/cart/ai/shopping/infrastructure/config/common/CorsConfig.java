package cart.ai.shopping.infrastructure.config.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Value("${app.cors.allow-credentials:true}")
    private boolean allowCredentials;

    @Value("${app.cors.allowed-headers:*}")
    private String[] allowedHeaders;

    @Value("${app.cors.allowed-methods:GET,POST,PUT,DELETE}")
    private String[] allowedMethods;

    @Value("${app.cors.allowed-origins:http://localhost:5174}")
    private String[] allowedOrigins;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowCredentials(allowCredentials)
                        .allowedHeaders(allowedHeaders)
                        .allowedMethods(allowedMethods)
                        .allowedOrigins(allowedOrigins);
            }
        };
    }
}
