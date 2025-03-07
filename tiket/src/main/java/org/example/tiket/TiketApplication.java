package org.example.tiket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TiketApplication {

    public static void main(String[] args) {
        SpringApplication.run(TiketApplication.class, args);
    }

}
