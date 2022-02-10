package com.rabbitmq.demo.controller;

import com.rabbitmq.demo.domain.Person;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/test/{name}")
    public String testAPI(@PathVariable("name") String name) {
        Person person = new Person(1l, name);

        // this convert and send using simple message converter
        // simple message converter only supports string,byte array and serializable object
        // so Person should be implemented Serializable interface
        rabbitTemplate.convertAndSend("Mobile", person); // pass message to queue

        rabbitTemplate.convertAndSend("Direct-Exchange","mobile",person);// pass to direct exchange
        rabbitTemplate.convertAndSend("Fanout-Exchange","",person);// pass to fanout exchange
        rabbitTemplate.convertAndSend("Topic-Exchange","tv.mobile.ac",person);// pass to topic exchange

        // headers exchange is sightly different from above approaches

        return "SUCCESS";
    }
}
