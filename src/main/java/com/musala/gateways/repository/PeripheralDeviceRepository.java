package com.musala.gateways.repository;

import com.musala.gateways.model.Gateway;
import com.musala.gateways.model.PeripheralDevice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PeripheralDeviceRepository extends JpaRepository<PeripheralDevice, Long> {
    Optional<PeripheralDevice> findByUid(Long uid);

    Long countByGateway(Gateway gateway);
}
