package routingkey;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReceiverRoutingKey {
    private static final String BINDING_KEY_NAME = "RoutingKeyTest";
    private static final String NAME_EXCHANGE = "directExchange";
    private static final Logger logger = Logger.getLogger(ReceiverRoutingKey.class.getName());

    public static void main(String[] args) throws Exception {
        connectionFactory(getConnectionFactory(), NAME_EXCHANGE, BINDING_KEY_NAME, logger);
    }

    static void connectionFactory(ConnectionFactory connectionFactory, String nameExchange, String bindingKeyName, Logger logger) throws TimeoutException {

        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {

            // Creating the queue that will be used
            // The server will determine a random name for the queue, so it'll be temporary
            String nameQueue = channel.queueDeclare().getQueue();

            // Declaring the exchange
            channel.exchangeDeclare(nameExchange, "direct");
            channel.queueBind(nameQueue, nameExchange, bindingKeyName);

            // Setting up the callback logging to confirm that the message has been received
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.log(Level.INFO, "[*] Received message: {0}", message);
            };

            // Consuming the messages
            channel.basicConsume(nameQueue, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException e) {
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