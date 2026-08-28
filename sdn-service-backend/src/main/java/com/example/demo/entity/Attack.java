package com.example.demo.entity;



import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "attacks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Network in which the attack was detected.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id", nullable = false)
    private Network network;

    /*
     * Type of attack detected.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AttackType attackType;

    /*
     * Source of the malicious/suspicious traffic.
     */
    @Column(length = 45)
    private String sourceIp;

    /*
     * Target of the attack.
     */
    @Column(length = 45)
    private String targetIp;

    /*
     * Severity assigned by the detection/decision logic.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Severity severity;

    /*
     * Confidence of the detection.
     * Example: 0.95 = 95%
     */
    private Double confidence;

    /*
     * When the attack was detected.
     */
    @Column(nullable = false)
    private LocalDateTime detectedAt;

    /*
     * Current state of the attack.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AttackStatus status;

    /*
     * Optional description explaining why
     * the attack was classified.
     */
    @Column(length = 500)
    private String description;

    @PrePersist
    protected void onCreate() {

        if (detectedAt == null) {
            detectedAt = LocalDateTime.now();
        }

        if (status == null) {
            status = AttackStatus.DETECTED;
        }
    }

    public enum AttackType {
        DDOS,
        PORT_SCAN,
        TRAFFIC_ANOMALY
    }

    public enum Severity {
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    public enum AttackStatus {
        DETECTED,
        MITIGATING,
        MITIGATED,
        FAILED,
        RESOLVED
    }
}
