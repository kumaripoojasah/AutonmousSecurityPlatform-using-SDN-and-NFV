package com.example.demo.service;

import com.example.demo.dto.traffic.ModelInputFeatures;
import com.example.demo.kafka.event.TrafficEvent;
import org.springframework.stereotype.Service;

@Service
public class TrafficFeatureTransformerService {

    public ModelInputFeatures transform(TrafficEvent event) {
        if (event == null) {
            return null;
        }

        // Extract packet, byte, and timestamp metrics safely
        long pktCount = event.getPacketCount() != null ? event.getPacketCount() : 0L;
        long bCount = event.getByteCount() != null ? event.getByteCount() : 0L;
        String timeStr = event.getTimestamp() != null ? event.getTimestamp().toString() : "N/A";

        // Extract existing rates and flags
        double fwdRate = event.getPacketRate() != null ? event.getPacketRate() : 0.0;
        double bwdRate = event.getBackwardPacketRate() != null ? event.getBackwardPacketRate() : 0.0;
        double stdDev = event.getPacketLengthStdDev() != null ? event.getPacketLengthStdDev() : 0.0;
        int synCount = event.getUniquePorts() != null ? event.getUniquePorts() : 0;
        int ackCount = event.getTcpFlagAckCount() != null ? event.getTcpFlagAckCount() : 0;
        double iat = event.getInterArrivalTimeMs() != null ? event.getInterArrivalTimeMs() : 0.0;

        // Feature Engineering: Calculate SYN-to-ACK ratio
        double synToAckRatio = (ackCount == 0) ? synCount : (double) synCount / ackCount;

        // Feature Engineering: Volume threshold flag
        boolean highVolume = fwdRate > 100.0;

        return ModelInputFeatures.builder()
                .deviceId(event.getDeviceId() != null ? event.getDeviceId() : "UNKNOWN")
                .packetCount(pktCount)
                .byteCount(bCount)
                .timestamp(timeStr)
                .forwardPacketRate(fwdRate)
                .backwardPacketRate(bwdRate)
                .packetLengthStdDev(stdDev)
                .tcpFlagSynCount(synCount)
                .tcpFlagAckCount(ackCount)
                .interArrivalTimeMs(iat)
                .synToAckRatio(synToAckRatio)
                .isHighVolume(highVolume)
                .build();
    }
}