package com.musala.gateways.utils;


import com.musala.gateways.model.Gateway;
import com.musala.gateways.openapi.model.GatewayRequest;
import com.musala.gateways.openapi.model.GatewayResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GatewayMapper {
    private final ModelMapper gateWayModelMapper;

    @Autowired
    public GatewayMapper(ModelMapper gateWayModelMapper) {
        this.gateWayModelMapper = gateWayModelMapper;
    }

    public GatewayResponse map(Gateway gateway) {
        return gateWayModelMapper.map(gateway, GatewayResponse.class);
    }

    public GatewayResponse map(GatewayRequest gateway) {
        return gateWayModelMapper.map(gateway, GatewayResponse.class);
    }


    public Gateway mapGateway(GatewayRequest gateway) {
        return gateWayModelMapper.map(gateway, Gateway.class);
    }
}
