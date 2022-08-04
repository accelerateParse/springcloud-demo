package com.prey.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author ： YaoJiangXiao
 * @data 2022/07/05/11:14
 * @description:
 */
@EnableKafka
@EnableScheduling
@SpringBootApplication(exclude= DataSourceAutoConfiguration.class)
public class KafkaApplication {
    public static void main(String[] args) {
        SpringApplication.run(KafkaApplication.class, args);
    }
}
