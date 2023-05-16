package RoutingKey;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderRoutingKey {

    private static final String NAME_EXCHANGE = "directExchange";
    private static final String ROUTING_KEY = "RoutingKeyTest";
    private static final String SECOND_ROUTING_KEY = "SecondRoutingKeyTest";

    public static void main(String[] args) {

        // creating the connection
        // setting creation information
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        try (Connection connection = factory.newConnection()) {

            //creating a new channel
            Channel channel = connection.createChannel();
            System.out.println(channel);

            //declaring the exchange that will be used
            channel.exchangeDeclare(NAME_EXCHANGE, "direct");

            //sending the messages
            String message = "Hello! This is a RabbitMQ system";
            String secondMessage = "Hello! This is a routing key based system";
            channel.basicPublish(NAME_EXCHANGE, ROUTING_KEY, null, message.getBytes());
            channel.basicPublish(NAME_EXCHANGE, SECOND_ROUTING_KEY, null, secondMessage.getBytes());

            System.out.print("[x] Sent  '" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
