package com.musala.gateways.utils;

import com.musala.gateways.model.Gateway;
import com.musala.gateways.openapi.model.GatewayResponse;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestHelper {

    public List<Gateway> gatewayList() {
        List<Gateway> gateways = new ArrayList<>();
        Gateway gateway1 = new Gateway();
        gateway1.setId(1L);
        gateway1.setName("name1");
        gateway1.setSerialNumber("serial1");
        gateway1.setIpv4("192.168.1.2");
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
}
