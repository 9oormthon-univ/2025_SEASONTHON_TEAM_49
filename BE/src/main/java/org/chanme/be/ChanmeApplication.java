package org.chanme.be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "org.chanme.be")
public class ChanmeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChanmeApplication.class, args);
    }
}