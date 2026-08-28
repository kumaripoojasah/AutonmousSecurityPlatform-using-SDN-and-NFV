package com.example.demo.repository;
import com.example.demo.entity.SecurityRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SecurityRuleRepository extends JpaRepository<SecurityRule, Long> {

    /*
     * Fetch active security rules for a given network.
     */
    List<SecurityRule> findByNetworkIdAndStatus(Long networkId, SecurityRule.RuleStatus status);

    /*
     * Find rules associated with a specific mitigation action.
     */
    List<SecurityRule> findByMitigationActionId(Long mitigationActionId);

    /*
     * Retrieve all rules currently active in the SDN controller.
     */
    List<SecurityRule> findByStatus(SecurityRule.RuleStatus status);
}
