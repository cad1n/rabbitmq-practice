package routingkey;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SecondReceiverRoutingKey {
    private static final String BINDING_KEY_NAME = "SecondRoutingKeyTest";
    private static final String NAME_EXCHANGE = "directExchange";
    private static final Logger logger = Logger.getLogger(SecondReceiverRoutingKey.class.getName());

    public static void main(String[] args) throws Exception {
        ReceiverRoutingKey.connectionFactory(getConnectionFactory(), NAME_EXCHANGE, BINDING_KEY_NAME, logger);
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