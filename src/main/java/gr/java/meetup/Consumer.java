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
                from("rabbitmq://localhost/test?queue=meetup")
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
