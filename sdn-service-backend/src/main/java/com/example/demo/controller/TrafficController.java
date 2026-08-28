package com.example.demo.controller;

import com.example.demo.dto.traffic.TrafficResponse;
import com.example.demo.dto.traffic.ModelInputFeatures; // Added import for ML features
import com.example.demo.service.TrafficService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/traffic")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows cross-origin requests from the React frontend
public class TrafficController {

    private final TrafficService trafficService;

    @GetMapping("/device/{deviceId}")
    public List<TrafficResponse> getTraffic(
            @PathVariable Long deviceId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {

        return trafficService.getDeviceTraffic(
                deviceId,
                start,
                end
        );
    }

    /**
     * Live telemetry polling endpoint for the React SOC dashboard
     */
    @GetMapping("/live")
    public ResponseEntity<TrafficResponse> getLiveTrafficStats() {
        TrafficResponse response = TrafficResponse.builder()
                .deviceId(String.valueOf(4L))
                .deviceName("device-4")
                .sourceIp("10.0.0.5")
                .destinationIp("10.0.0.5")
                .timestamp(LocalDateTime.now())
                .forwardPacketRate(120.5)
                .tcpFlagSynCount(22)
                .synToAckRatio(22.0)
                .isHighVolume(true)
                .featureVector(new double[]{0.0, 0.0, 120.5, 0.0, 0.0, 22.0, 0.0, 0.0, 22.0})
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * Raw ML Model Input Features endpoint matching your transformer service
     */
    @GetMapping("/traffic")
    public ResponseEntity<ModelInputFeatures> getTrafficStats() {
        double fwdRate = 120.5;
        int synCount = (int)(Math.random() * 50 + 5);
        int ackCount = 2;
        double synToAckRatio = (double) synCount / ackCount;

        ModelInputFeatures features = ModelInputFeatures.builder()
                .deviceId("device-" + (int)(Math.random() * 5 + 1))
                .packetCount(150L)
                .byteCount(4500L)
                .forwardPacketRate(fwdRate)
                .backwardPacketRate(45.0)
                .packetLengthStdDev(12.4)
                .tcpFlagSynCount(synCount)
                .tcpFlagAckCount(ackCount)
                .interArrivalTimeMs(15.2)
                .synToAckRatio(synToAckRatio)
                .isHighVolume(fwdRate > 100.0)
                .build();

        return ResponseEntity.ok(features);
    }
}
/*package com.example.demo.controller;


import com.example.demo.dto.traffic.TrafficResponse;
import com.example.demo.service.TrafficService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/traffic")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Allows cross-origin requests from the React frontend
public class TrafficController {

    private final TrafficService trafficService;

    @GetMapping("/device/{deviceId}")
    public List<TrafficResponse> getTraffic(
            @PathVariable Long deviceId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end) {

        return trafficService.getDeviceTraffic(
                deviceId,
                start,
                end
        );
    }

    /**
     * Live telemetry polling endpoint for the React SOC dashboard

    @GetMapping("/live")
    public ResponseEntity<TrafficResponse> getLiveTrafficStats() {
        TrafficResponse response = TrafficResponse.builder()
                .deviceId(String.valueOf(4L)) // Using Long as per Pooja's TrafficResponse DTO structure
                .deviceName("device-4")
                .sourceIp("10.0.0.5")
                .destinationIp("10.0.0.5")
                .timestamp(LocalDateTime.now())
                .forwardPacketRate(120.5)
                .tcpFlagSynCount(22)
                .synToAckRatio(22.0)
                .isHighVolume(true)
                .featureVector(new double[]{0.0, 0.0, 120.5, 0.0, 0.0, 22.0, 0.0, 0.0, 22.0})
                .build();

        return ResponseEntity.ok(response);
    }
}
*/