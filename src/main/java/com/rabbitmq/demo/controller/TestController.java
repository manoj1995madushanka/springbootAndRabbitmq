package com.rabbitmq.demo.controller;

import com.rabbitmq.demo.domain.Person;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

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

    /**
     * publisher to headers exchange
     * */
    @GetMapping("/testHeadersExchange/{name}")
    public String testHeaderExchange(@PathVariable("name") String name) throws IOException {
        Person person = new Person(2l,name);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutput objectOutput = new ObjectOutputStream(byteArrayOutputStream);
        objectOutput.writeObject(person);
        objectOutput.flush();
        objectOutput.close();

        byte[] messageArray = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        // publish object byte array to headers exchange
        Message message = MessageBuilder.withBody(messageArray)
                .setHeader("item1","mobile")
                .setHeader("item2","television")
                .build();

        rabbitTemplate.send("Headers-Exchange","",message);

        return "SUCCESS";
    }
}
