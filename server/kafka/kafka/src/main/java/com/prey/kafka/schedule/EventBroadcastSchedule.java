package com.prey.kafka.schedule;


import com.prey.kafka.comsumer.KafkaProducer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author ： YaoJiangXiao
 * @data 2022/07/18/16:24
 * @description:
 */
 @Component
public class EventBroadcastSchedule {

    @Resource
    private KafkaProducer producer;

    String s = "{\"msg\":\"hello\"}";

    @Scheduled(cron = "*/2 * * * * ?", zone = "GMT-8:00")
    // @Scheduled(cron = "0 */2 * * * ?", zone = "GMT-8:00")
    public void eventBroadcast(){
        producer.sendMessageSync("b_box",s);
    }

}
