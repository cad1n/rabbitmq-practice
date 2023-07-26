package dlx;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverDLX {

    private static final String CONSUMER_QUEUE = "consumerQueue";
    private static final Logger logger = Logger.getLogger(ReceiverDLX.class.getName());

    public static void main(String[] args) throws Exception {
        try {
            ConnectionFactory factory = getConnectionFactory();
            try (Connection connection = factory.newConnection();
                 Channel channel = connection.createChannel()) {

                consumeMessages(channel);
            }
        } catch (IOException | TimeoutException e) {
            logger.log(Level.WARNING, String.format("Error while creating channel or connection: %s", e.getMessage()));
        }
    }

    private static void consumeMessages(Channel channel) throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            logger.log(Level.INFO, String.format("[*] Received message: %s ", message));
        };

        channel.basicConsume(CONSUMER_QUEUE, true, deliverCallback, consumerTag -> {
        });
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