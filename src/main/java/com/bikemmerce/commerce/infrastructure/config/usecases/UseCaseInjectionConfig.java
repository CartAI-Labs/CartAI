package com.bikemmerce.commerce.infrastructure.config.usecases;

import com.bikemmerce.commerce.application.annotations.UseCase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "com.bikemmerce.commerce.application.usecases.*",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = UseCase.class
        )
)
public class UseCaseInjectionConfig {
}
