package br.com.catsanddogs.catsanddogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CatsAndDogsApplication {

    public static void main(String[] args) {
        SpringApplication.run(CatsAndDogsApplication.class, args);
    }

}
