package com.musala.gateways.utils;

import com.musala.gateways.GatewaysApplication;
import com.musala.gateways.config.AppConfig;
import com.musala.gateways.model.Gateway;
import com.musala.gateways.openapi.model.GatewayResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {GatewaysApplication.class, AppConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class GatewayMapperTest {
    @Spy
    private ModelMapper gateWayModelMapper;
    @InjectMocks
    private GatewayMapper gatewayMapper;
    private TestHelper testHelper;

    @BeforeEach
    public void setup() {
        testHelper = new TestHelper();
    }

    @Test
    void map() {
        Gateway gateway = testHelper.gatewayList().get(0);
        GatewayResponse map = gatewayMapper.map(gateway);
        assertThat(map.getName()).isEqualTo(gateway.getName());
        assertThat(map.getSerialNumber()).isEqualTo(gateway.getSerialNumber());
        assertThat(map.getIpv4()).isEqualTo(gateway.getIpv4());
    }

    @Test
    void testMap() {
    }

    @Test
    void mapGateway() {
    }
}
