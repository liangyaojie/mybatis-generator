package com.mybatis.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mybatis.generator.mapper")
public class MybatisGeneratorMain {

	public static void main(String[] args) {
		SpringApplication.run(MybatisGeneratorMain.class, args);
	}

}
