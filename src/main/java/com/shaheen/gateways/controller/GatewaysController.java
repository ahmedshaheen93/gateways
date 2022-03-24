package com.shaheen.gateways.controller;

import com.shaheen.gateways.openapi.api.GatewaysApi;
import com.shaheen.gateways.openapi.model.*;
import com.shaheen.gateways.service.GateWayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
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
    public ResponseEntity<GatewayResponse> getGatewayBySerialNumber(String serialNumber) {
        return ResponseEntity.ok(gateWayService.getGatewayBySerialNumber(serialNumber));
    }

    @Override
    public ResponseEntity<GatewayResponse> updateGateway(String serialNumber, GatewayUpdateRequest gatewayRequest) {
        return ResponseEntity.ok(gateWayService.update(serialNumber, gatewayRequest));
    }

    @Override
    public ResponseEntity<Void> deleteGateway(String serialNumber) {
        gateWayService.deleteBySerialNumber(serialNumber);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<PeripheralResponse>> getAllPeripherals(String serialNumber) {
        return ResponseEntity.ok(gateWayService.getPeripheralDevices(serialNumber));
    }

    @Override
    public ResponseEntity<PeripheralResponse> createPeripheral(String serialNumber, PeripheralRequest peripheralRequest) {
        PeripheralResponse response = gateWayService.addPeripheralDevice(serialNumber, peripheralRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public ResponseEntity<PeripheralResponse> getPeripheralBySerialNumberAndUid(String serialNumber, BigDecimal uid) {
        return ResponseEntity.ok(gateWayService.getPeripheralDevicesBySerialNumberAndUid(serialNumber, uid.longValue()));
    }

    @Override
    public ResponseEntity<PeripheralResponse> updatePeripheral(String serialNumber, BigDecimal uid, PeripheralUpdateRequest peripheralUpdateRequest) {
        return ResponseEntity.ok(gateWayService.updatePeripheralDevice(serialNumber, uid.longValue(), peripheralUpdateRequest));
    }

    @Override
    public ResponseEntity<Void> deletePeripheral(String serialNumber, BigDecimal uid) {
        gateWayService.deletePeripheralDeviceBySerialNumberAndUid(serialNumber, uid.longValue());
        return ResponseEntity.noContent().build();
    }
}
