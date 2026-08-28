package com.example.demo.service;


import com.example.demo.dto.traffic.TrafficResponse;
import com.example.demo.entity.TrafficSnapshot;
import com.example.demo.repository.TrafficSnapshotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrafficService {

    private final TrafficSnapshotRepository trafficRepository;

    public List<TrafficResponse> getDeviceTraffic(
            Long deviceId,
            LocalDateTime start,
            LocalDateTime end) {

        return trafficRepository
                .findByDeviceIdAndTimestampBetweenOrderByTimestampAsc(
                        deviceId,
                        start,
                        end
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TrafficResponse toResponse(TrafficSnapshot traffic) {

        return TrafficResponse.builder()
                .deviceId(String.valueOf(traffic.getDevice().getId()))
                .deviceName(traffic.getDevice().getName())
                .timestamp(traffic.getTimestamp())
                .packetCount(traffic.getPacketCount())
                .byteCount(traffic.getByteCount())
                .packetRate(traffic.getPacketRate())
                .byteRate(traffic.getByteRate())
                .activeFlows(Double.valueOf(traffic.getActiveFlows()))
                .uniqueSources(traffic.getUniqueSources())
                .uniquePorts(traffic.getUniquePorts())
                .protocol(traffic.getProtocol())
                .build();
    }
}