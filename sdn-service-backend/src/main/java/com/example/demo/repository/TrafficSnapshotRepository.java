package com.example.demo.repository;


import com.example.demo.entity.TrafficSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface TrafficSnapshotRepository
        extends JpaRepository<TrafficSnapshot, Long> {

    /*
     * Get traffic snapshots belonging to a particular device.
     */
    List<TrafficSnapshot> findByDeviceIdOrderByTimestampDesc(Long deviceId);

    /*
     * Get traffic snapshots within a specific time period.
     */
    List<TrafficSnapshot> findByTimestampBetweenOrderByTimestampAsc(
            LocalDateTime start,
            LocalDateTime end
    );

    /*
     * Get traffic history for one device within a time period.
     */
    List<TrafficSnapshot> findByDeviceIdAndTimestampBetweenOrderByTimestampAsc(
            Long deviceId,
            LocalDateTime start,
            LocalDateTime end
    );
}