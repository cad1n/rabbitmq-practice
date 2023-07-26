package dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderDLX {

    private static final String NAME_EXCHANGE = "mainExchange";
    private static final Logger logger = Logger.getLogger(SenderDLX.class.getName());

    public static void main(String[] args) throws IOException, TimeoutException {
        // creating the connection
        // setting creation information
        try {
            ConnectionFactory factory = setConnectionFactory();
            createChannelExchangeAndPublishMessage(factory);
        } catch (IOException | TimeoutException e) {
            logger.log(Level.WARNING, String.format("Error while creating channel or connection: %s", e.getMessage()));
        }
    }

    private static void createChannelExchangeAndPublishMessage(ConnectionFactory factory) throws IOException, TimeoutException {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            //declaring the exchange that will be used
            channel.exchangeDeclare(NAME_EXCHANGE, "topic");

            //creating the message
            String message = "This is a test";
            String routingKey = "consumerBindingKey.consumer";

            //sending the message
            channel.basicPublish(NAME_EXCHANGE, routingKey, null, message.getBytes());

            logger.info("[X] Done");
        }
    }

    private static ConnectionFactory setConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);
        return factory;
    }
}
