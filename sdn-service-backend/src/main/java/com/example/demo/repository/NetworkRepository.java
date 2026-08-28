package com.example.demo.repository;



import com.example.demo.entity.Network;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NetworkRepository extends JpaRepository<Network, Long> {

    List<Network> findByStatus(Network.NetworkStatus status);

    List<Network> findByEnvironment(String environment);
}