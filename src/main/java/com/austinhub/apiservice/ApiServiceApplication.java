package com.austinhub.apiservice;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiServiceApplication.class, args);
        Date expirationTime =
                Date.from(LocalDate.now().plusWeeks(2).atStartOfDay(ZoneId.of("UTC")).toInstant());
    }
}
