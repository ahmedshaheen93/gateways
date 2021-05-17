package com.musala.gateways.utils;

import com.musala.gateways.model.PeripheralDevice;
import com.musala.gateways.openapi.model.PeripheralRequest;
import com.musala.gateways.openapi.model.PeripheralResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PeripheralDeviceMapper {
    @Autowired
    @Qualifier("peripheralDeviceModelMapper")
    private ModelMapper peripheralDeviceModelMapper;

    public PeripheralResponse map(PeripheralDevice peripheralDevice) {
        return peripheralDeviceModelMapper.map(peripheralDevice, PeripheralResponse.class);
    }

    public PeripheralDevice mapPeripheralDevice(PeripheralRequest peripheralRequest) {
        return peripheralDeviceModelMapper.map(peripheralRequest, PeripheralDevice.class);
    }
}
