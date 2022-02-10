package com.rabbitmq.demo.service;

import com.rabbitmq.demo.domain.Person;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitmqConsumer {

    /**
     * listen message of Mobile queue
     * we do not need to call this because @RabbitListener listen to queue eventually
     * */
    @RabbitListener(queues = "Mobile")// this will continuously listen to the Mobile queue
    public void getMessage(Person person) // in here we know about message data type we can use that also
            // means we can use byte[] directly without Person object
    {

        System.out.println(person.getName());
    }
}
