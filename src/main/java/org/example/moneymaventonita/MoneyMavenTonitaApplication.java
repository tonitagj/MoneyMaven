package org.example.moneymaventonita;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MoneyMavenTonitaApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneyMavenTonitaApplication.class, args);
    }

}
