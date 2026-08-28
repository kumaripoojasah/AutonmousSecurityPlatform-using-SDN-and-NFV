package com.example.demo.kafka.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrafficEvent {

    // Changed from Long to String to handle values like "device-1"
    @JsonProperty("deviceId")
    private String deviceId;

    private Long networkId;

    @JsonProperty("sourceIp")
    private String sourceIp;

    @JsonProperty("destinationIp")
    private String destinationIp;

    private Long packetCount;

    private Long byteCount;

    @JsonProperty("forwardPacketRate")
    private Double packetRate;

    @JsonProperty("backwardPacketRate")
    private Double backwardPacketRate;

    private Double byteRate;

    @JsonProperty("packetLengthStdDev")
    private Double packetLengthStdDev;

    private Integer activeFlows;

    private Integer uniqueSources;

    @JsonProperty("tcpFlagSynCount")
    private Integer uniquePorts;

    @JsonProperty("tcpFlagAckCount")
    private Integer tcpFlagAckCount;

    @JsonProperty("interArrivalTimeMs")
    private Double interArrivalTimeMs;

    @JsonProperty("protocol")
    private String protocol;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
}