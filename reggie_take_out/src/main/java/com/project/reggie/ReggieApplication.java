package com.project.reggie;


import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@Slf4j //interesting facts: https://medium.com/javarevisited/why-i-dont-recommend-the-lombok-6e6d4a2c54dc
@SpringBootApplication //necessary if this class is a springboot startup class
@ServletComponentScan
public class ReggieApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReggieApplication.class, args);
        log.info("$$$ Startup Success... $$$");
    }
}
