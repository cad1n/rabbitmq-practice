package WorkQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderWork {

    private static final String NAME_QUEUE = "Work";

    public static void main(String[] args) {

        // creating the connection
        // setting creation information
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        try (Connection connection = factory.newConnection()) {

            //creating a new channel
            Channel channel = connection.createChannel();
            System.out.println(channel);

            //declaring the queue that will be used
            channel.queueDeclare(NAME_QUEUE, false, false, false, null);

            //sending the message
            String message = ".......";
            channel.basicPublish("", NAME_QUEUE, null, message.getBytes());

            System.out.print("[x] Sent  '" + message + "'");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
