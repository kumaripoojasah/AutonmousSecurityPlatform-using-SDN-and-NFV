package com.example.demo.repository;



import com.example.demo.entity.Attack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttackRepository extends JpaRepository<Attack, Long> {

    List<Attack> findByNetworkIdOrderByDetectedAtDesc(Long networkId);

    List<Attack> findByAttackTypeOrderByDetectedAtDesc(
            Attack.AttackType attackType
    );

    List<Attack> findByStatusOrderByDetectedAtDesc(
            Attack.AttackStatus status
    );

    List<Attack> findBySeverityOrderByDetectedAtDesc(
            Attack.Severity severity
    );

    List<Attack> findTop10ByOrderByDetectedAtDesc();
}