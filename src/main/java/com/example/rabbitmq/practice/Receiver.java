package com.example.rabbitmq.practice;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Receiver {
    private static final String NAME_QUEUE = "HELLO";
    private static final Logger logger = Logger.getLogger(Receiver.class.getName());

    public static void main(String[] args) throws Exception {
        // creating the connection
        // setting creation information

        ConnectionFactory factory = getConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            //declaring the queue that will be used
            channel.queueDeclare(NAME_QUEUE, false, false, false, null);

            //setting up the callback logging to confirm that the message has been received
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
               logger.log(Level.INFO, String.format("[*] Received message: %s ", message));
            };

            //consuming the messages
            channel.basicConsume(NAME_QUEUE, true, deliverCallback, consumerTag -> {
            });
        }
    }

    private static ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);
        return factory;
    }
}
