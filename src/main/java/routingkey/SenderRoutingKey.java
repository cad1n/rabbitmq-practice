package routingkey;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderRoutingKey {

    private static final String NAME_EXCHANGE = "directExchange";
    private static final String ROUTING_KEY = "RoutingKeyTest";
    private static final String SECOND_ROUTING_KEY = "SecondRoutingKeyTest";

    private static final Logger logger = Logger.getLogger(SenderRoutingKey.class.getName());

    public static void main(String[] args) {

        // creating the connection
        // setting creation information
        ConnectionFactory factory = getConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            logger.log(Level.INFO, String.format("Channel has been created: %s ", channel));

            //declaring the exchange that will be used
            channel.exchangeDeclare(NAME_EXCHANGE, "direct");

            //sending the messages
            String message = "Hello! This is a RabbitMQ system";
            String secondMessage = "Hello! This is a routing key based system";
            channel.basicPublish(NAME_EXCHANGE, ROUTING_KEY, null, message.getBytes());
            channel.basicPublish(NAME_EXCHANGE, SECOND_ROUTING_KEY, null, secondMessage.getBytes());

            logger.log(Level.INFO, String.format("[x] Sent:  %s", message));
        } catch (Exception e) {
            logger.log(Level.WARNING, String.format("Error while creating channel or connection: %s", e.getMessage()));
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
