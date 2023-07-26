package pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderPubSub {

    private static final String NAME_EXCHANGE = "fanoutExchange";
    private static final Logger logger = Logger.getLogger(SenderPubSub.class.getName());

    public static void main(String[] args) {

        // creating the connection
        // setting creation information
        ConnectionFactory factory = getConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()){

            //declaring the exchange that will be used
            channel.exchangeDeclare(NAME_EXCHANGE, "fanout");

            //sending the message
            String message = "Hello! This is a pub/sub system";
            channel.basicPublish(NAME_EXCHANGE, "", null, message.getBytes());

            logger.log(Level.INFO, String.format("[x] Sent: %s ", message));
        } catch (IOException | TimeoutException e) {
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
