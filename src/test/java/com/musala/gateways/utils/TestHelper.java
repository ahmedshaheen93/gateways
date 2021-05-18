package com.musala.gateways.utils;

import com.musala.gateways.model.Gateway;
import com.musala.gateways.model.PeripheralDevice;
import com.musala.gateways.model.Status;
import com.musala.gateways.openapi.model.GatewayResponse;
import com.musala.gateways.openapi.model.PeripheralResponse;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TestHelper {

    public List<Gateway> gatewayList() {
        List<Gateway> gateways = new ArrayList<>();
        Gateway gateway1 = new Gateway();
        gateway1.setId(1L);
        gateway1.setName("name1");
        gateway1.setSerialNumber("serial1");
        gateway1.setIpv4("192.168.1.2");
        gateway1.setPeripheralDevices(Set.copyOf(peripheralDevices()));
        Gateway gateway2 = new Gateway();
        gateway2.setId(2L);
        gateway2.setName("name2");
        gateway2.setSerialNumber("serial2");
        gateway2.setIpv4("192.168.1.3");
        Gateway gateway3 = new Gateway();
        gateway3.setId(3L);
        gateway3.setName("name3");
        gateway3.setSerialNumber("serial3");
        gateway3.setIpv4("192.168.1.4");
        gateways.add(gateway1);
        gateways.add(gateway2);
        gateways.add(gateway3);
        return gateways;
    }

    public List<GatewayResponse> mappedGateways() {
        ModelMapper modelMapper = new ModelMapper();
        return gatewayList().stream()
                .map(gateway -> modelMapper.map(gateway, GatewayResponse.class))
                .collect(Collectors.toList());
    }

    public List<PeripheralDevice> peripheralDevices() {
        List<PeripheralDevice> response = new ArrayList<>();
        PeripheralDevice peripheralDevice1 = new PeripheralDevice();
        peripheralDevice1.setUid(1L);
        peripheralDevice1.setVendor("vendor1");
        peripheralDevice1.setCreatedDate(LocalDate.now());
        peripheralDevice1.setStatus(Status.ONLINE);
        //
        PeripheralDevice peripheralDevice2 = new PeripheralDevice();
        peripheralDevice2.setUid(2L);
        peripheralDevice2.setVendor("vendor2");
        peripheralDevice2.setCreatedDate(LocalDate.now());
        peripheralDevice2.setStatus(Status.OFFLINE);
        //
        PeripheralDevice peripheralDevice3 = new PeripheralDevice();
        peripheralDevice3.setUid(3L);
        peripheralDevice3.setVendor("vendor3");
        peripheralDevice3.setCreatedDate(LocalDate.now());
        peripheralDevice3.setStatus(Status.ONLINE);
        response.add(peripheralDevice1);
        response.add(peripheralDevice2);
        response.add(peripheralDevice3);
        return response;
    }

    public List<PeripheralResponse> mappedPeripheralDevices() {
        ModelMapper modelMapper = new ModelMapper();
        return peripheralDevices().stream()
                .map(peripheralDevice ->
                        modelMapper.map(peripheralDevice, PeripheralResponse.class))
                .collect(Collectors.toList());
    }
}
