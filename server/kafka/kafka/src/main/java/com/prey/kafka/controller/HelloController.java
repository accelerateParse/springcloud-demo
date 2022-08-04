package com.prey.kafka.controller;

import com.prey.kafka.comsumer.KafkaProducer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author ： YaoJiangXiao
 * @data 2022/07/05/11:28
 * @description:
 */
@RestController
public class HelloController {

    @Resource
    private KafkaProducer kafkaProducer;

    @GetMapping("/hell0")
    public String helloWorld(){
        String msg = "time:" + System.currentTimeMillis();
        kafkaProducer.sendMessageSync("q_box","time:" + System.currentTimeMillis());
        return "success";
    }

}
