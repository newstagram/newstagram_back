package com.newstagram.rss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EntityScan(basePackages = {"com.newstagram.domain", "com.newstagram.rss"})
@EnableJpaRepositories(basePackages = {"com.newstagram.domain", "com.newstagram.rss"})
public class RssCollectorApplication {

    public static void main(String[] args) {
        SpringApplication.run(RssCollectorApplication.class, args);
    }

}
