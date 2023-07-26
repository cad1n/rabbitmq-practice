package pubconfirmation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecondSenderPubConfirmation {

    private static final String NAME_EXCHANGE = "fanoutExchange";

    public static void main(String[] args) {
        Logger logger = Logger.getLogger(SecondSenderPubConfirmation.class.getName());

        ConnectionFactory factory = getConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            // Declaring the exchange that will be used
            channel.exchangeDeclare(NAME_EXCHANGE, "fanout");

            // Creating the message
            String message = "This is my ";
            int setOfMessages = 10;
            int outOfMessages = 0;
            String bodyMessage;

            // Sending the message
            for (int i = 0; i < setOfMessages; ++i) {
                bodyMessage = message + i;

                channel.basicPublish(NAME_EXCHANGE, "", null, bodyMessage.getBytes());
                logger.log(Level.INFO, String.format("[X] Sending the message: %s", bodyMessage));
                outOfMessages++;

                if (outOfMessages == setOfMessages) {
                    // Wait 5 secs for confirmation
                    channel.waitForConfirmsOrDie(5_000);
                    logger.log(Level.INFO, String.format("[v] Sent %d messages!", setOfMessages));
                    outOfMessages = 0;
                }
            }

            if (outOfMessages != 0) {
                channel.waitForConfirmsOrDie(5_000);
                logger.log(Level.INFO, String.format("[v] Sent %d messages!", outOfMessages));
            }

            logger.log(Level.INFO, "[v] Done!");
        } catch (IOException | TimeoutException | InterruptedException e) {
            logger.log(Level.SEVERE, String.format("Error while publishing messages: %s", e.getMessage()));
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