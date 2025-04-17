package com.ztbdz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.ztbdz.*.mapper")
public class ZtbdzProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZtbdzProjectApplication.class, args);
    }

}
