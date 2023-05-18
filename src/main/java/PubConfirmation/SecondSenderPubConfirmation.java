package PubConfirmation;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class SecondSenderPubConfirmation {

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
            String message = "This is my ";
            int setOfMessages = 10;
            int outOfMessages = 0;
            String bodyMessage;

            //sending the message
            for (int i = 0; i < setOfMessages; ++i) {
                bodyMessage = message + i;

                channel.basicPublish(NAME_EXCHANGE, "", null, bodyMessage.getBytes());
                System.out.println("[X] Sending the message: " + bodyMessage);
                outOfMessages++;

                if (outOfMessages == setOfMessages) {
                    //wait 5 secs for confirmation
                    channel.waitForConfirmsOrDie(5_000);
                    System.out.println("[v] The message has been sent!");
                    outOfMessages = 0;
                }
            }

            if (outOfMessages != 0) {

                channel.waitForConfirmsOrDie(5_000);
                System.out.println("[v] The message has been sent!");
            }

            System.out.print("[v] Done!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
