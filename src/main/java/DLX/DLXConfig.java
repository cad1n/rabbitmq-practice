package DLX;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class DLXConfig {

    //DLX
    private static final String DLX_NAME = "DLXExchange";
    private static final String DLX_QUEUE = "dlxQueue";
    private static final String DLX_BINDING_KEY = "dlxBindingKey";

    //Main exchange
    private static final String EXCHANGE_NAME = "mainExchange";

    //Consumer
    private static final String CONSUMER_QUEUE = "consumerQueue";
    private static final String CONSUMER_BINDING_KEY = "consumerBindingKey";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("admin");
        factory.setPassword("pass123");
        factory.setPort(5672);

        Connection connection = factory.newConnection();

        Channel channel = connection.createChannel();

        //Declaring exchanges (DLX and Main)
        channel.exchangeDeclare(DLX_NAME, "topic");
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");

        //Queues declaration
        channel.queueDeclare(DLX_QUEUE, false, false, false, null);
        Map<String, Object> map = new HashMap<>();
        map.put("x-message-ttl", 10000);
        map.put("x-dead-letter-exchange", DLX_NAME);
        map.put("x-dead-letter-routing-key", DLX_BINDING_KEY);
        channel.queueDeclare(CONSUMER_QUEUE, false, false, false, map);

        //DLX binding key
        channel.queueBind(DLX_QUEUE, DLX_NAME, DLX_BINDING_KEY+".#");
        channel.queueBind(CONSUMER_QUEUE, EXCHANGE_NAME, CONSUMER_BINDING_KEY+".#");

        connection.close();
    }
}
