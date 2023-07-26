package workqueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SenderWork {

    private static final String NAME_QUEUE = "Work";
    private static final Logger logger = Logger.getLogger(SenderWork.class.getName());

    public static void main(String[] args) {

        // creating the connection
        // setting creation information
        ConnectionFactory factory = getConnectionFactory();

        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            //declaring the queue that will be used
            channel.queueDeclare(NAME_QUEUE, false, false, false, null);

            //sending the message
            String message = ".......";
            channel.basicPublish("", NAME_QUEUE, null, message.getBytes());

            logger.log(Level.INFO, String.format("[x] Sent: %s", message));
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error while consuming messages", e);
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
