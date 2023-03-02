package com.example.demo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.amqp.support.ConsumerTagStrategy;

import java.nio.charset.StandardCharsets;

public class Receiver {
    private static String NAME_QUEUE = "HELLO";

    public static void main(String[] args) throws Exception {
        // creating the connection
        // setting creation information

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        Connection connection = factory.newConnection();
        //System.out.println(connection.hashCode());

        //creating a new channel
        Channel channel1 = connection.createChannel();
        System.out.println(channel1);

        //declaring the queue that will be used
        channel1.queueDeclare(NAME_QUEUE, false, false, false, null);

        DeliverCallback deliverCallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received message '" + message + "'");
        };

        //consuming the messages
        channel1.basicConsume(NAME_QUEUE, true, deliverCallback, ConsumerTag -> {
        });
    }
}
