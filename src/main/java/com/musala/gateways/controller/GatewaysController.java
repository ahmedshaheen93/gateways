package com.musala.gateways.controller;

import com.musala.gateways.openapi.api.GatewaysApi;
import com.musala.gateways.openapi.model.GatewayRequest;
import com.musala.gateways.openapi.model.GatewayResponse;
import com.musala.gateways.openapi.model.GatewayUpdateRequest;
import com.musala.gateways.service.GateWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GatewaysController implements GatewaysApi {

    private final GateWayService gateWayService;

    @Autowired
    public GatewaysController(GateWayService gateWayService) {
        this.gateWayService = gateWayService;
    }

    @Override
    public ResponseEntity<List<GatewayResponse>> getAllGateways() {
        List<GatewayResponse> allGateways = gateWayService.getAllGateways();
        return ResponseEntity.ok(allGateways);
    }

    @Override
    public ResponseEntity<GatewayResponse> createGateway(GatewayRequest gatewayRequest) {
        GatewayResponse response = gateWayService.save(gatewayRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<Void> deleteGateway(String serialNumber) {
        gateWayService.deleteBySerialNumber(serialNumber);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<GatewayResponse> getGatewayBySerialNumber(String serialNumber) {
        return ResponseEntity.ok(gateWayService.getGatewayBySerialNumber(serialNumber));
    }

    @Override
    public ResponseEntity<GatewayResponse> updateGateway(String serialNumber, GatewayUpdateRequest gatewayRequest) {
        return ResponseEntity.ok(gateWayService.update(serialNumber, gatewayRequest));
    }
}
