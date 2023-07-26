package topicexchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderTopic {

    private static final String NAME_EXCHANGE = "topicExchange";
    private static final Logger logger = Logger.getLogger(SenderTopic.class.getName());


    public static void main(String[] args) {

        // creating the connection
        // setting creation information
        ConnectionFactory factory = getConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

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

            logger.log(Level.INFO, String.format("[x] Sent: %s ", message));
        } catch (Exception e) {
            logger.log(Level.SEVERE, String.format("Error while consuming messages: %s", e.getMessage()));
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
