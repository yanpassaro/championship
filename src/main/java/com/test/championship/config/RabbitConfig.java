package com.test.championship.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;


public class RabbitConfig {

    @Bean
    public Queue queue() {
        return new Queue("game", true);
    }
}
