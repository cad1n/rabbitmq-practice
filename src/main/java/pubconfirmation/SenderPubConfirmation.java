package pubconfirmation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderPubConfirmation {

    private static final String NAME_EXCHANGE = "fanoutExchange";
    private static final Logger logger = Logger.getLogger(SenderPubConfirmation.class.getName());

    public static void main(String[] args) {
        ConnectionFactory factory = getConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declaring the exchange that will be used
            channel.exchangeDeclare(NAME_EXCHANGE, "fanout");

            // Creating the messages
            ArrayList<String> messages = new ArrayList<>(3);
            messages.add("Hello World");
            messages.add("This is the second message!");
            messages.add("This is the final message!");

            // Sending the messages
            for (String body : messages) {
                channel.basicPublish(NAME_EXCHANGE, "", null, body.getBytes());
                logger.log(Level.INFO, "[X] Sending the message: {0}", body);

                // Wait 5 secs for confirmation
                channel.waitForConfirmsOrDie(5_000);
                logger.log(Level.INFO, "[v] The message has been sent!");
            }

            logger.log(Level.INFO, "[v] Done!");
        } catch (Exception e) {
            logger.log(Level.WARNING, String.format("Error while creating channel or connection: %s", e.getMessage()));
            Thread.currentThread().interrupt();
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