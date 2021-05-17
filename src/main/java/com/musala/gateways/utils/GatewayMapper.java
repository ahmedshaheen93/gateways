package com.musala.gateways.utils;


import com.musala.gateways.model.Gateway;
import com.musala.gateways.openapi.model.GatewayRequest;
import com.musala.gateways.openapi.model.GatewayResponse;
import org.springframework.stereotype.Component;

@Component
public class GatewayMapper {
    public GatewayResponse map(Gateway gateway) {
        GatewayResponse response = new GatewayResponse();
        response.setSerialNumber(gateway.getSerialNumber());
        response.setName(gateway.getName());
        response.setIpv4(gateway.getIpv4());
        return response;
    }

    public GatewayResponse map(GatewayRequest gateway) {
        GatewayResponse response = new GatewayResponse();
        response.setSerialNumber(gateway.getSerialNumber());
        response.setName(gateway.getName());
        response.setIpv4(gateway.getIpv4());
        return response;
    }


    public Gateway mapGateway(GatewayRequest gateway) {
        Gateway response = new Gateway();
        response.setSerialNumber(gateway.getSerialNumber());
        response.setName(gateway.getName());
        response.setIpv4(gateway.getIpv4());
        return response;
    }
}
