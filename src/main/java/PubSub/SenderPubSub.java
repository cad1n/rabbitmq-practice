package PubSub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderPubSub {

    private static final String NAME_EXCHANGE = "fanoutExchange";

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
            channel.exchangeDeclare(NAME_EXCHANGE, "fanout");

            //sending the message
            String message = "Hello! This is a pub/sub system";
            channel.basicPublish(NAME_EXCHANGE, "", null, message.getBytes());

            System.out.print("[x] Sent  '" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
