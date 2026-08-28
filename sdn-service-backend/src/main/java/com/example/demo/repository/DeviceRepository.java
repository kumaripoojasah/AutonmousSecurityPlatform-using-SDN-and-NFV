package com.example.demo.repository;


import com.example.demo.entity.Device;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByNetworkId(Long networkId);

    List<Device> findByDeviceType(Device.DeviceType deviceType);

    List<Device> findByStatus(Device.DeviceStatus status);

    List<Device> findByNetworkIdAndStatus(
            Long networkId,
            Device.DeviceStatus status
    );
}