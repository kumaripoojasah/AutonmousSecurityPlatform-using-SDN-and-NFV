package com.example.demo.entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mitigation_actions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MitigationAction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * The attack that triggered this mitigation action.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attack_id", nullable = false)
    private Attack attack;

    /*
     * Type of action taken by the system.
     * Example: BLOCK_IP, RATE_LIMIT, REDIRECT_TO_HONEYPOT
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ActionType actionType;

    /*
     * Specific target parameter for the action (e.g., target IP, MAC address, or switch port).
     */
    @Column(length = 100)
    private String actionTarget;

    /*
     * Value/Limit associated with the action (e.g., rate limit value like "100kbps").
     */
    @Column(length = 50)
    private String actionValue;

    /*
     * Current execution status of the mitigation action.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ActionStatus status;

    /*
     * When the mitigation action was executed.
     */
    @Column(nullable = false)
    private LocalDateTime executedAt;

    /*
     * Optional message or details regarding the execution outcome.
     */
    @Column(length = 500)
    private String executionDetails;

    @PrePersist
    protected void onCreate() {
        if (executedAt == null) {
            executedAt = LocalDateTime.now();
        }
        if (status == null) {
            status = ActionStatus.PENDING;
        }
    }

    public enum ActionType {
        BLOCK_IP,
        RATE_LIMIT,
        REDIRECT_TRAFFIC,
        DROP_PACKETS,
        ISOLATE_PORT
    }

    public enum ActionStatus {
        PENDING,
        APPLIED,
        FAILED,
        REVERTED
    }
}