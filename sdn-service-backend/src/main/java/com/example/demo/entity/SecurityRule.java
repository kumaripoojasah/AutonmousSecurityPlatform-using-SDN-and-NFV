package com.example.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "security_rules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * Network where this security rule is deployed.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "network_id", nullable = false)
    private Network network;

    /*
     * Optional link to the specific mitigation action that created this rule.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mitigation_action_id")
    private MitigationAction mitigationAction;

    /*
     * Name or identifier for the rule (e.g., "Block-Attacker-10.0.0.25").
     */
    @Column(nullable = false, length = 100)
    private String ruleName;

    /*
     * Target IP, CIDR block, or port pattern.
     */
    @Column(length = 45)
    private String sourceIp;

    @Column(length = 45)
    private String destinationIp;

    @Column(length = 10)
    private String protocol;

    private Integer destinationPort;

    /*
     * Action performed by the network switch/firewall.
     * Example: DROP, PASS, RATE_LIMIT
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RuleAction action;

    /*
     * Current status on the physical/virtual switch.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RuleStatus status;

    /*
     * Priority of the flow rule in OpenFlow / SDN Switch.
     */
    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = RuleStatus.ACTIVE;
        }
        if (priority == null) {
            priority = 100;
        }
    }

    public enum RuleAction {
        DROP,
        ALLOW,
        LIMIT,
        REDIRECT
    }

    public enum RuleStatus {
        ACTIVE,
        EXPIRED,
        DISABLED
    }
}