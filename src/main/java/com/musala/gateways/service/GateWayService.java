package com.musala.gateways.service;

import com.musala.gateways.openapi.model.GatewayRequest;
import com.musala.gateways.openapi.model.GatewayResponse;
import com.musala.gateways.openapi.model.GatewayUpdateRequest;

import java.util.List;

public interface GateWayService {
    List<GatewayResponse> getAllGateways();

    GatewayResponse getGatewayBySerialNumber(String serialNumber);

    GatewayResponse save(GatewayRequest gatewayRequest);

    void deleteBySerialNumber(String serialNumber);

    GatewayResponse update(String serialNumber, GatewayUpdateRequest gatewayRequest);

}
