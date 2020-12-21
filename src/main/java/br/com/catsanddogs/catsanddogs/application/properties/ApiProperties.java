package br.com.catsanddogs.catsanddogs.application.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.service")
@Data
public class ApiProperties {
    private String cats;
    private String dogs;
}
