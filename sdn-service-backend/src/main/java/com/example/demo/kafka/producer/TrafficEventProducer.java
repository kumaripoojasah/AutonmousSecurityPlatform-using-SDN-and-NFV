package com.example.demo.kafka.producer;


import com.example.demo.config.KafkaConfig;
import com.example.demo.kafka.event.TrafficEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TrafficEventProducer {

    private final KafkaTemplate<String, TrafficEvent> kafkaTemplate;

    public void sendTrafficEvent(TrafficEvent event) {

        String key = String.valueOf(event.getDeviceId());

        kafkaTemplate.send(
                KafkaConfig.TRAFFIC_TOPIC,
                key,
                event
        );
    }
}