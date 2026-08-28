package com.example.demo.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String TRAFFIC_TOPIC = "traffic-events";
    public static final String ATTACK_TOPIC = "attack-events";
    public static final String MITIGATION_TOPIC = "mitigation-events";

    @Bean
    public NewTopic trafficTopic() {
        return TopicBuilder.name(TRAFFIC_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic attackTopic() {
        return TopicBuilder.name(ATTACK_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic mitigationTopic() {
        return TopicBuilder.name(MITIGATION_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}