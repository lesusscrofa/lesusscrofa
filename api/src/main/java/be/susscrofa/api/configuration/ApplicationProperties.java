package be.susscrofa.api.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("app")
@Data
public class ApplicationProperties {

    private Web web;

    @Data
    public static class Web {
        private String corsAllowedOrigin;
    }
}
