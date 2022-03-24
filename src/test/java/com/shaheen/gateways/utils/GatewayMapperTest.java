package com.shaheen.gateways.utils;

import com.shaheen.gateways.model.Gateway;
import com.shaheen.gateways.openapi.model.GatewayResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class GatewayMapperTest {

    private GatewayMapper gatewayMapper;
    private TestHelper testHelper;

    @BeforeEach
    public void setup() {
        testHelper = new TestHelper();
        gatewayMapper = new GatewayMapper();
    }

    @Test
    void map() {
        Gateway gateway = testHelper.gatewayList().get(0);
        GatewayResponse map = gatewayMapper.map(gateway);
        assertThat(map.getName()).isEqualTo(gateway.getName());
        assertThat(map.getSerialNumber()).isEqualTo(gateway.getSerialNumber());
        assertThat(map.getIpv4()).isEqualTo(gateway.getIpv4());
    }

}
