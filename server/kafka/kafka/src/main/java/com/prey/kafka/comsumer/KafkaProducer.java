package com.prey.kafka.comsumer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author ： YaoJiangXiao
 * @data 2022/07/05/11:17
 * @description:
 */
@Component
public class KafkaProducer {

    @Resource
    private KafkaTemplate<String, String> kafkaTemplate;



    /**
     * 发送消息（同步）
     * @param topic 主题
     * @param message 值
     */
    public void sendMessageSync(String topic,  String message)  {
        //可以指定最长等待时间，也可以不指定
        kafkaTemplate.send(topic, message);
        System.out.println("sendMessage to "+topic +" -->>" + message);
    }
}
