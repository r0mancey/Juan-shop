package com.example.juan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@EnableTransactionManagement
@ServletComponentScan
public class JuanApplication {

    public static void main(String[] args) {
        SpringApplication.run(JuanApplication.class, args);
    }

}
