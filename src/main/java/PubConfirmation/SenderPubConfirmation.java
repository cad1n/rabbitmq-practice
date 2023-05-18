package PubConfirmation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Vector;

public class SenderPubConfirmation {

    private static final String NAME_EXCHANGE = "fanoutExchange";

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
            //AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();

            //declaring the exchange that will be used
            channel.exchangeDeclare(NAME_EXCHANGE, "fanout");

            //creating the message
            Vector<String> message = new Vector<>(3);
            message.add("Hello World");
            message.add("This is the second message!");
            message.add("This is the final message!");

            //sending the message
            for (int i = 0; i < 3; ++i) {
                String body = message.get(i);

                channel.basicPublish(NAME_EXCHANGE, "", null, body.getBytes());
                System.out.println("[X] Sending the message: " + body);

                //wait 5 secs for confirmation
                channel.waitForConfirmsOrDie(5_000);
                System.out.println("[v] The message has been sent!");
            }


            System.out.print("[v] Done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
