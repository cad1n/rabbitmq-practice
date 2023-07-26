package pubsub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SecondReceiverPubSub {
    private static final String NAME_QUEUE = "Broadcast";
    private static final String NAME_EXCHANGE = "fanoutExchange";

    private static final Logger logger = Logger.getLogger(SecondReceiverPubSub.class.getName());


    public static void main(String[] args) throws Exception {

        // creating the connection
        // setting creation information
        try (Channel channel = createConnectionFactoryAndChannel()) {

            //setting up the callback logging to confirm that the message has been received
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                if (!message.isEmpty()) {
                    logger.info("[*] Received message");
                }
            };
            //consuming the messages
            assert channel != null;
            channel.basicConsume(NAME_QUEUE, true, deliverCallback, consumerTag -> {
            });
        } catch (IOException | TimeoutException e) {
            logger.log(Level.WARNING, String.format("Error while creating channel or connection: %s", e.getMessage()));
        }
    }

    private static Channel createConnectionFactoryAndChannel() throws IOException, TimeoutException {
        try {
            ConnectionFactory factory = setConnectionFactory();
            return createChannelQueueAndExchange(factory);
        } catch (IOException | TimeoutException e) {
            logger.log(Level.WARNING, String.format("Error while creating channel or connection: %s", e.getMessage()));
            return null;
        }
    }

    private static Channel createChannelQueueAndExchange(ConnectionFactory factory) throws IOException, TimeoutException {
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {
            declareQueueAndExchange(channel);
            return channel;
        }
    }

    private static void declareQueueAndExchange(Channel channel) throws IOException {
        //declaring the queue
        channel.queueDeclare(NAME_QUEUE, false, false, false, null);

        //declaring the exchange
        channel.exchangeDeclare(NAME_EXCHANGE, "fanout");

        //binding queue and exchange
        channel.queueBind(NAME_QUEUE, NAME_EXCHANGE, "");
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
