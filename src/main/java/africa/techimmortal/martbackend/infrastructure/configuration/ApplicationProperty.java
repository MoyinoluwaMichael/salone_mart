package africa.techimmortal.martbackend.infrastructure.configuration;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.math.BigDecimal;

@ConfigurationProperties("mart")
@Configuration
@Data
@EnableJpaAuditing
public class ApplicationProperty {

    private String jwtSigningSecret;
    private String adminInvitationClientUrl;

}
