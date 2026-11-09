package com.sysrefugios.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SysrefugiosApplication {

    public static void main(String[] argumentos) {
        SpringApplication.run(SysrefugiosApplication.class, argumentos);
    }

}
