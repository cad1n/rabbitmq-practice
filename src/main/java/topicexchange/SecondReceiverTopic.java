package topicexchange;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SecondReceiverTopic {
    private static final String NAME_EXCHANGE = "topicExchange";
    private static final Logger logger = Logger.getLogger(SecondReceiverTopic.class.getName());


    public static void main(String[] args) throws Exception {

        // creating the connection
        // setting creation information
        ConnectionFactory factory = getConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            //declaring the queue that will be used
            // the server will determine a random name for the queue, so it'll be temporary
            String nameQueue = channel.queueDeclare().getQueue();
            String bindingKey = "#.rabbit";

            //declaring the exchange
            channel.exchangeDeclare(NAME_EXCHANGE, "topic");
            channel.queueBind(nameQueue, NAME_EXCHANGE, bindingKey);

            //setting up the callback logging to confirm that the message has been received
            DeliverCallback deliverCallback = (consumerTag, delivery) -> {
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                logger.log(Level.INFO, "[*] Received message: {0}", message);
            };

            //consuming the messages
            channel.basicConsume(nameQueue, true, deliverCallback, consumerTag -> {
            });
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
