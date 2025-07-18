package com.ztbdz;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@MapperScan("com.ztbdz.*.mapper")
@EnableCaching
public class ZtbdzProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZtbdzProjectApplication.class, args);
    }

}
