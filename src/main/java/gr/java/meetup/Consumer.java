package gr.java.meetup;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class Consumer {

    @Bean
    public RouteBuilder routeBuilder() {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("rabbitmq://localhost/testDirect?queue=meetup&exchangeType=direct")
                        .convertBodyTo(String.class)
                        .process(exchange -> {
                            System.out.printf("Received message from RabbitMQ: %s%n", exchange.getIn().getBody());
                        });
            }
        };
    }


    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(Consumer.class, args);
    }
}



/**
 * Direct uri: rabbitmq://localhost/testDirect?queue=meetup&exchangeType=direct
 * Fanout uri: rabbitmq://localhost/testFanout?exchangeType=fanout
 * Topic uri:
 * rabbitmq://localhost/testTopic?queue=meetup&exchangeType=topic&routingKey=kosmas.*
 * rabbitmq://localhost/testTopic?queue=meetup&exchangeType=topic&routingKey=spyros.*
 * rabbitmq://localhost/testTopic?exchangeType=topic&routingKey=*.pizza
 */
