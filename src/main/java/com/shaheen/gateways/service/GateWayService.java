package com.shaheen.gateways.service;

import com.shaheen.gateways.openapi.model.*;

import java.util.List;

public interface GateWayService {
    List<GatewayResponse> getAllGateways();

    GatewayResponse getGatewayBySerialNumber(String serialNumber);

    GatewayResponse save(GatewayRequest gatewayRequest);

    void deleteBySerialNumber(String serialNumber);

    GatewayResponse update(String serialNumber, GatewayUpdateRequest gatewayRequest);

    List<PeripheralResponse> getPeripheralDevices(String serialNumber);

    PeripheralResponse addPeripheralDevice(String serialNumber, PeripheralRequest peripheralRequest);

    PeripheralResponse getPeripheralDevicesBySerialNumberAndUid(String serialNumber, Long uid);

    PeripheralResponse updatePeripheralDevice(String serialNumber, Long uid, PeripheralUpdateRequest peripheralUpdateRequest);

    void deletePeripheralDeviceBySerialNumberAndUid(String serialNumber, Long uid);
}
