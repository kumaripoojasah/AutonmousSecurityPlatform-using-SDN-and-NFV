package com.example.demo.repository;



import com.example.demo.entity.MitigationAction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MitigationActionRepository extends JpaRepository<MitigationAction, Long> {

    /*
     * Retrieve all mitigation actions performed for a specific attack.
     */
    List<MitigationAction> findByAttackIdOrderByExecutedAtDesc(Long attackId);

    /*
     * Find actions by their current status (e.g., APPLIED, PENDING).
     */
    List<MitigationAction> findByStatusOrderByExecutedAtDesc(MitigationAction.ActionStatus status);

    /*
     * Find actions matching a specific action type.
     */
    List<MitigationAction> findByActionTypeOrderByExecutedAtDesc(MitigationAction.ActionType actionType);

    /*
     * Retrieve the most recent mitigation actions across the system.
     */
    List<MitigationAction> findTop10ByOrderByExecutedAtDesc();
}