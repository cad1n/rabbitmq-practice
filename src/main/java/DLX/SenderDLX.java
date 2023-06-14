package DLX;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class SenderDLX {

    private static final String NAME_EXCHANGE = "mainExchange";

    public static void main(String[] args) throws IOException, TimeoutException {

        // creating the connection
        // setting creation information
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        Channel channel;
        try (Connection connection = factory.newConnection()) {

            //creating a new channel
            channel = connection.createChannel();


            //declaring the exchange that will be used
            channel.exchangeDeclare(NAME_EXCHANGE, "topic");

            //creating the message
            String message = "This is a test";
            String routingKey = "consumerBindingKey.consumer";

            //sending the message
            channel.basicPublish(NAME_EXCHANGE, routingKey, null, message.getBytes());

            System.out.print("[X] Done");
        }
    }
}
