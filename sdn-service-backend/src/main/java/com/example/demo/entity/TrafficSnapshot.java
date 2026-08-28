package com.example.demo.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "traffic_snapshots")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrafficSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Device from which this traffic information was collected.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "device_id", nullable = false)
    private Device device;

    /*
     * Time at which the traffic statistics were collected.
     */
    @Column(nullable = false)
    private LocalDateTime timestamp;

    /*
     * Number of packets observed during the monitoring interval.
     */
    @Column(nullable = false)
    private Long packetCount;

    /*
     * Number of bytes observed during the monitoring interval.
     */
    @Column(nullable = false)
    private Long byteCount;

    /*
     * Calculated packet rate.
     * Example: 12500 packets/sec
     */
    @Column(nullable = false)
    private Double packetRate;

    /*
     * Calculated byte rate.
     * Example: 4500000 bytes/sec
     */
    @Column(nullable = false)
    private Double byteRate;

    /*
     * Number of currently active flows.
     */
    @Column(nullable = false)
    private Integer activeFlows;

    /*
     * Number of unique source IP addresses observed.
     */
    @Column(nullable = false)
    private Integer uniqueSources;

    /*
     * Number of unique destination ports accessed.
     */
    @Column(nullable = false)
    private Integer uniquePorts;

    /*
     * Protocol information if available.
     * Example: TCP, UDP, ICMP
     */
    @Column(length = 20)
    private String protocol;

    /*
     * Optional source IP associated with this observation.
     */
    @Column(length = 45)
    private String sourceIp;

    /*
     * Optional destination IP.
     */
    @Column(length = 45)
    private String destinationIp;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }
}