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
public class Producer implements InitializingBean{

    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);

    private static int counter = 0;

    @EndpointInject(uri = "rabbitmq://localhost/testDirect?queue=meetup&exchangeType=direct")
    private ProducerTemplate producer;

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Producer.class, args);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        executorService.scheduleWithFixedDelay(() -> {
            final String message = "Hello World " + counter++;

            System.out.printf("Sending message to RabbitMQ: %s%n", message);

            producer.sendBody(message);
        }, 1, 1, TimeUnit.SECONDS);
    }
}


/**
 * Direct uri: rabbitmq://localhost/testDirect?queue=meetup&exchangeType=direct
 * Fanout uri: rabbitmq://localhost/testFanout?exchangeType=fanout
 */
