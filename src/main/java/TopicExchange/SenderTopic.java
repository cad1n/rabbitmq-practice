package TopicExchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderTopic {

    private static final String NAME_EXCHANGE = "topicExchange";


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
            channel.exchangeDeclare(NAME_EXCHANGE, "topic");

            //creating the messages
            String routingKey1 = "quick.orange.rabbit";
            String message = "Hello! This is the original message";
            String secondMessage = "Hello! This is the message with the routing key:" + routingKey1;
            String routingKey2 = "quick.rabbit";

            //sending the messages
            channel.basicPublish(NAME_EXCHANGE, routingKey1, null, message.getBytes());
            channel.basicPublish(NAME_EXCHANGE, routingKey2, null, secondMessage.getBytes());

            System.out.print("[x] Sent  '" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
