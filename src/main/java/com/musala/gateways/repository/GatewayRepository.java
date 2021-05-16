package com.musala.gateways.repository;

import com.musala.gateways.model.Gateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GatewayRepository extends JpaRepository<Gateway, Long> {
    Optional<Gateway> findBySerialNumber(String serialNumber);
}
