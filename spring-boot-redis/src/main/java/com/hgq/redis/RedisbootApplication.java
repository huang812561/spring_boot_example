package com.hgq.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

//@ImportResource(value = "/spring/spring-redis.xml")
@SpringBootApplication
public class RedisbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisbootApplication.class, args);
	}
}
