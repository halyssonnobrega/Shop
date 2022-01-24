package com.demo.shop.config.jms.consumers;

import com.demo.shop.business.entity.ItemEO;
import com.demo.shop.business.repository.ItemRepository;
import com.demo.shop.resources.model.ItemRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Component
public class ShopJmsListener {

    private final ItemRepository itemRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ShopJmsListener.class);

    @Autowired
    public ShopJmsListener(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @JmsListener(destination = "${jms.msg.consumer.destination.shop}", concurrency = "${jms.msg.consumer.concurrency}")
    public void onMessage(Message message) {
        Date receiveTime = new Date();

        if (message instanceof TextMessage) {
            TextMessage tm = (TextMessage) message;
            try {
                Random rand = new Random();
                int upperbound = 5;
                int int_random = rand.nextInt(upperbound);
                int_random ++;

                LOGGER.info(
                        "==========START CONSUMER SHOP RECEIVED MESSAGE==========  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(receiveTime)
                                + " with message content of: " + tm.getText());

                Thread.sleep(int_random*3000);

                ObjectMapper objectMapper = new ObjectMapper();

                ItemRequest item = objectMapper.readValue(tm.getText(), ItemRequest.class);

                ItemEO itemEO = new ItemEO();
                itemEO.setName(item.getName());
                itemEO.setPrice(item.getPrice());
                itemEO.setCreatedOnDate(new Date());

                itemRepository.save(itemEO);

                LOGGER.info(
                        "==========END CONSUMER SHOP RECEIVED MESSAGE==========  " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(receiveTime)
                                + " with message content of: " + tm.getText());
            } catch (JMSException | InterruptedException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println(message.toString());
        }
    }
}