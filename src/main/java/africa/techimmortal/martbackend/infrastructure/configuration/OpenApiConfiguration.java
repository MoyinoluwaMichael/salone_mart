package africa.techimmortal.martbackend.infrastructure.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {
    @Bean
    public OpenAPI openApiDefinition() {
        return new OpenAPI()
                .info(new Info().title("Deli-Chops APP")
                        .version("1.0"));
    }
}
