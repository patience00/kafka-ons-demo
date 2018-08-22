package com.kafka.demo.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;


/**
 * @author:通天晓107
 * @date :2018-02-23 15:47
 * @version: 1.0
 */
@Component
@Slf4j
public class MsgConsumer {

    @KafkaListener(topics = {"${spring.kafka.template.default-topic}"})
    public void getMessage(String content) {
        log.info("消息 = " + content);

    }
}
