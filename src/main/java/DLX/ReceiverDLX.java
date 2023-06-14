package DLX;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;


public class ReceiverDLX {

    private static final String CONSUMER_QUEUE = "consumerQueue";

    public static void main(String[] args) throws Exception {

        // creating the connection
        // setting creation information
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        Connection connection = factory.newConnection();
        //System.out.println(connection.hashCode());

        //creating a new channel
        Channel channel = connection.createChannel();
        System.out.println(channel);

        //setting up the callback logging to confirm that the message has been received
        DeliverCallback deliverCallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received message '" + message + "'");
        };

        //consuming the messages
        channel.basicConsume(CONSUMER_QUEUE, true, deliverCallback, ConsumerTag -> {
        });
    }
}
