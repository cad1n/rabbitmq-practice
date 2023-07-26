package pubconfirmation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverPubConfirmation {
    private static final String NAME_EXCHANGE = "fanoutExchange";
    private static final Logger logger = Logger.getLogger(ReceiverPubConfirmation.class.getName());

    public static void main(String[] args) throws Exception {
        try {
            ConnectionFactory factory = getConnectionFactory();

            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                // Declaring the queue that will be used
                // The server will determine a random name for the queue, so it'll be temporary
                String nameQueue = channel.queueDeclare().getQueue();

                // Declaring the exchange
                channel.exchangeDeclare(NAME_EXCHANGE, "fanout");
                channel.queueBind(nameQueue, NAME_EXCHANGE, "");

                // Setting up the callback logging to confirm that the message has been received
                DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                    String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                    logger.log(Level.INFO, "[*] Received message: {0}", message);
                };

                // Consuming the messages
                channel.basicConsume(nameQueue, true, deliverCallback, consumerTag -> {
                });
            }
        } catch (IOException e) {
            logger.log(Level.SEVERE, String.format("Error while creating channel or connection: %s", e.getMessage()));
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