package WorkQueue;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.nio.charset.StandardCharsets;

public class ReceiverWork {
    private static final String NAME_QUEUE = "Work";

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') Thread.sleep(1000);
        }
    }

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

        //declaring the queue that will be used
        channel.queueDeclare(NAME_QUEUE, false, false, false, null);

        //setting up the callback logging to confirm that the message has been received
        DeliverCallback deliverCallback = (ConsumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("[*] Received message '" + message + "'");
            try {
                doWork(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("[X] Done");
            }
        };

        //consuming the messages
        boolean autoAck = true; //if ack is true the server should consider messages acknowledged once delivered
        channel.basicConsume(NAME_QUEUE, autoAck, deliverCallback, ConsumerTag -> {
        });
    }
}
