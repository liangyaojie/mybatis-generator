package com.mybatis.generator;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ResultVO
 *
 * @author LYJ
 * @date 2020/7/16 8:58
 * @since 1.0
 */
@SpringBootApplication
@MapperScan("com.mybatis.generator.mapper")
public class MybatisGeneratorMain {

	public static void main(String[] args) {
		SpringApplication.run(MybatisGeneratorMain.class, args);
	}

}
