package com.prey.kafka.producer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author ： YaoJiangXiao
 * @data 2022/07/05/11:17
 * @description:
 */
// @Component
public class KafkaConsumer {
    @KafkaListener(topics = "q_device_states", groupId = "busService")
    public void comsumer(String message) {
        System.out.println("q_box comuser-->" + message);
    }
}
