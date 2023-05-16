package PubSub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;


public class SecondReceiverPubSub {
    private static final String NAME_QUEUE = "Broadcast";
    private static final String NAME_EXCHANGE = "fanoutExchange";


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

        //declaring the queue
        channel.queueDeclare(NAME_QUEUE, false, false, false, null);

        //declaring the exchange
        channel.exchangeDeclare(NAME_EXCHANGE, "fanout");

        channel.queueBind(NAME_QUEUE, NAME_EXCHANGE, "");

        //setting up the callback logging to confirm that the message has been received
        DeliverCallback deliverCallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received message '" + message + "'");
        };

        //consuming the messages
        channel.basicConsume(NAME_QUEUE, true, deliverCallback, ConsumerTag -> {
        });
    }
}