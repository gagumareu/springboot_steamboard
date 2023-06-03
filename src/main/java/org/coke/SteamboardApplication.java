package org.coke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SteamboardApplication {

    public static void main(String[] args) {
        SpringApplication.run(SteamboardApplication.class, args);
    }

}
