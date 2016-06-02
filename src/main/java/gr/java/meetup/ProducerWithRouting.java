package gr.java.meetup;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableAutoConfiguration
public class ProducerWithRouting implements InitializingBean{

    private static final String routingKeys[] = {"kosmas.seefood", "kosmas.fastfood", "kosmas.pizza",
                                                 "spyros.seefood", "spyros.fastfood", "spyros.pizza"};

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    private static int counter = 0;

    @EndpointInject(uri = "rabbitmq://localhost/testTopic?exchangeType=topic")
    private ProducerTemplate producer;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ProducerWithRouting.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService.scheduleWithFixedDelay(() -> {
            String routingKey = routingKeys[counter % routingKeys.length];

            final String message = "Hello World " + counter++ + " " + routingKey;

            System.out.printf("Sending message to RabbitMQ: %s%n", message);

            producer.sendBodyAndHeader(message, "rabbitmq.ROUTING_KEY", routingKey);
        }, 1, 1, TimeUnit.SECONDS);
    }
}


/**
 * Direct uri: rabbitmq://localhost/testDirect?queue=meetup&exchangeType=direct
 * Fanout uri: rabbitmq://localhost/testFanout?queue=meetup&exchangeType=fanout
 * Topic uri: rabbitmq://localhost/testTopic?exchangeType=topic
 */
