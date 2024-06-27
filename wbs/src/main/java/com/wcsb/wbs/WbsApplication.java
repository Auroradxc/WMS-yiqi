package com.wcsb.wbs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan(basePackages = "com.wcsb.wbs.mapper")
public class WbsApplication {

    public static void main(String[] args) {
        SpringApplication.run(WbsApplication.class, args);
    }

}
