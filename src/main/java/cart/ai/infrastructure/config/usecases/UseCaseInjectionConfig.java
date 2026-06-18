package cart.ai.infrastructure.config.usecases;

import cart.ai.application.annotations.UseCase;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(
        basePackages = "cart.ai.application.usecases.*",
        includeFilters = @ComponentScan.Filter(
                type = FilterType.ANNOTATION,
                classes = UseCase.class
        )
)
public class UseCaseInjectionConfig {
}
