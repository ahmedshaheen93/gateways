package com.shaheen.gateways.utils;

import com.shaheen.gateways.model.PeripheralDevice;
import com.shaheen.gateways.model.Status;
import com.shaheen.gateways.openapi.model.PeripheralRequest;
import com.shaheen.gateways.openapi.model.PeripheralResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PeripheralDeviceMapper {

    public PeripheralDevice mapPeripheralDevice(PeripheralRequest peripheralRequest) {
        PeripheralDevice peripheralDevice = new PeripheralDevice();
        peripheralDevice.setUid(peripheralRequest.getUid().longValue());
        peripheralDevice.setStatus(Status.getByString(peripheralRequest.getStatus().getValue()));
        return peripheralDevice;
    }

    public PeripheralResponse map(PeripheralDevice peripheralDevice) {
        PeripheralResponse response = new PeripheralResponse();
        response.setUid(BigDecimal.valueOf(peripheralDevice.getUid()));
        response.setVendor(peripheralDevice.getVendor());
        response.setCreatedDate(peripheralDevice.getCreatedDate());
        response.setStatus(com.shaheen.gateways.openapi.model.Status.fromValue(peripheralDevice.getStatus().getValue()));
        return response;
    }
}
