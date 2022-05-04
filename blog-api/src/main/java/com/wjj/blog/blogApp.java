package com.wjj.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.wjj.blog.dao.mapper")
public class blogApp {
    public static void main(String[] args) {
        SpringApplication.run(blogApp.class,args);
    }
}
