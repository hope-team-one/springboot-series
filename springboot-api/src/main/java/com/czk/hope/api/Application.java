package com.czk.hope.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @Author: Created by ChenZK
 * @Create: 2019/12/24 11:01
 */
@SpringBootApplication
@ComponentScan(basePackages = "com.czk.hope") //用于扫描@Controller @Service
@EnableJpaRepositories("com.czk.hope.entity.repository")   // 用于扫描Dao @Repository
@EntityScan("com.czk.hope.entity.model") // 用于扫描JPA实体类 @Entity
@EnableSwagger2
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
