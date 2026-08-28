package com.example.demo.kafka.consumer;

import com.example.demo.dto.traffic.ModelInputFeatures;
import com.example.demo.kafka.event.TrafficEvent;
import com.example.demo.service.DecisionModelService;
import com.example.demo.service.MitigationService;
import com.example.demo.service.PolicyEngineService;
import com.example.demo.service.TrafficFeatureTransformerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TrafficEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TrafficEventConsumer.class);

    private final TrafficFeatureTransformerService transformerService;
    private final DecisionModelService decisionModelService;
    private final PolicyEngineService policyEngineService;
    private final MitigationService mitigationService;

    public TrafficEventConsumer(TrafficFeatureTransformerService transformerService,
                                DecisionModelService decisionModelService,
                                PolicyEngineService policyEngineService,
                                MitigationService mitigationService) {
        this.transformerService = transformerService;
        this.decisionModelService = decisionModelService;
        this.policyEngineService = policyEngineService;
        this.mitigationService = mitigationService;
    }

    @KafkaListener(topics = "traffic-events", groupId = "decision-engine")
    public void consume(TrafficEvent event) {
        // 1. Log basic arrival info (original output format)
        logger.info("Traffic event received: device={}, packetRate={}, uniquePorts={}",
                event.getDeviceId(), event.getPacketRate(), event.getUniquePorts());

        // 2. Transform raw ODL traffic event into clean ML features
        ModelInputFeatures features = transformerService.transform(event);
        double[] featureVector = features.toFeatureVector();

        // 3. Display transformed feature data on terminal
        logger.info("------------------------------------------------------------------");
        logger.info(">>> INGESTED RAW EVENT [Device: {} | Src: {} -> Dst: {}]",
                features.getDeviceId(), event.getSourceIp(), event.getDestinationIp());
        logger.info(">>> RAW METRICS        : Packet Count = {}, Byte Count = {}, Timestamp = {}",
                features.getPacketCount(), features.getByteCount(), features.getTimestamp());
        logger.info(">>> TRANSFORMED ML VECTOR: {}", Arrays.toString(featureVector));
        logger.info(">>> DERIVED METRICS      : SYN/ACK Ratio = {}, HighVolume = {}",
                features.getSynToAckRatio(), features.isHighVolume());

        // 4. Send feature vector to Decision Engine (Python service)
        String prediction = decisionModelService.predict(features);
        logger.info(">>> DECISION ENGINE RESULT: {}", prediction);

        // 5. Evaluate threshold policies
        String mitigationAction = policyEngineService.determineMitigationAction(prediction, features);
        logger.info(">>> POLICY ENGINE ACTION  : {}", mitigationAction);

        // 6. Execute mitigation logic
        mitigationService.executeMitigation(mitigationAction, event, features);
        logger.info("------------------------------------------------------------------");
    }
}