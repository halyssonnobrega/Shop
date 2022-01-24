package com.demo.shop.config.jms.producers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class ShopJmsProducer {

    @Value("${jms.msg.consumer.destination.shop}")
    private String queueNameTest;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendMessage(String msg){
        System.out.println("==========SENDING MESSAGE TO ADD ITENS========== " + msg);
        jmsTemplate.convertAndSend(queueNameTest, msg);
    }
}