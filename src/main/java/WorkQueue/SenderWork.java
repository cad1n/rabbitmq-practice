package WorkQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SenderWork {

    private static final String NAME_QUEUE = "Work";

    public static void main(String[] args) throws Exception {
        // creating the connection
        // setting creation information

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        try (Connection connection = factory.newConnection()) {
            //System.out.println(connection.hashCode());

            //creating a new channel
            Channel channel1 = connection.createChannel();
            System.out.println(channel1);

            //declaring the queue that will be used
            channel1.queueDeclare(NAME_QUEUE, false, false, false, null);

            //sending the message
            String message = ".......";
            channel1.basicPublish("", NAME_QUEUE, null, message.getBytes());

            System.out.print("[x] sent  '" + message + "'");
        }
    }
}
