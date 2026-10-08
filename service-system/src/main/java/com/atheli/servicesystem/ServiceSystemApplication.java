package com.atheli.servicesystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication(scanBasePackages = {"com.atheli.servicesystem", "com.atheli.system"})
@EnableAsync
@MapperScan(basePackages = "com.atheli.servicesystem.mapper")
public class ServiceSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceSystemApplication.class, args);
    }

}
