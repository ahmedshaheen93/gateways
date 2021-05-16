package com.musala.gateways.service.impl;

import com.musala.gateways.exception.BadRequestException;
import com.musala.gateways.exception.NotFoundException;
import com.musala.gateways.model.Gateway;
import com.musala.gateways.openapi.model.GatewayRequest;
import com.musala.gateways.openapi.model.GatewayResponse;
import com.musala.gateways.openapi.model.GatewayUpdateRequest;
import com.musala.gateways.repository.GatewayRepository;
import com.musala.gateways.utils.GatewayMapper;
import com.musala.gateways.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class GateWayServiceImplTestMocked {

    private TestHelper testHelper;

    @Mock
    private GatewayRepository gatewayRepository;
    @Autowired
    private GatewayMapper gatewayMapper;

    private GateWayServiceImpl gateWayService;

    @BeforeEach
    public void setup() {
        testHelper = new TestHelper();
        gateWayService = new GateWayServiceImpl(gatewayRepository, gatewayMapper);
    }


    @Test
    void getAllGateways() {
        List<Gateway> gateways = testHelper.gatewayList();
        when(gatewayRepository.findAll()).thenReturn(gateways);
        List<GatewayResponse> allGateways = gateWayService.getAllGateways();
        assertThat(allGateways.size()).isEqualTo(3);
        for (int i = 0; i < gateways.size(); i++) {
            testEquals(gateways.get(i), allGateways.get(i));
        }
    }

    @Test
    void getAllGateways_EmptyList() {
        List<Gateway> gateways = new ArrayList<>();
        when(gatewayRepository.findAll()).thenReturn(gateways);
        List<GatewayResponse> allGateways = gateWayService.getAllGateways();
        assertThat(allGateways.size()).isEqualTo(0);
    }

    @Test
    void getGatewayBySerialNumber() {
        String serialNumber = "serial1";
        Optional<Gateway> first = testHelper.gatewayList().stream().findFirst();
        when(gatewayRepository.findBySerialNumber(serialNumber)).thenReturn(first);
        GatewayResponse gatewayBySerialNumber = gateWayService.getGatewayBySerialNumber(serialNumber);
        testEquals(first.get(), gatewayBySerialNumber);
    }

    @Test
    void getGatewayBySerialNumber_NotFoundException() {
        String serialNumber = "serial9";
        when(gatewayRepository.findBySerialNumber(serialNumber)).thenReturn(Optional.empty());

        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    gateWayService.getGatewayBySerialNumber(serialNumber);
                });
    }

    @Test
    void save() {
        // mocked request
        GatewayRequest gatewayRequest = new GatewayRequest();
        gatewayRequest.setSerialNumber("serial9");
        gatewayRequest.setName("name9");
        gatewayRequest.setIpv4("192.168.1.9");
        // mocked saved data
        Gateway gateway = new Gateway();
        gateway.setId(9L);
        gateway.setSerialNumber("serial9");
        gateway.setName("name9");
        gateway.setIpv4("192.168.1.9");
        when(gatewayRepository.findBySerialNumber(gatewayRequest.getSerialNumber())).thenReturn(Optional.empty());
        when(gatewayRepository.save(any())).thenReturn(gateway);
        GatewayResponse save = gateWayService.save(gatewayRequest);
        testEquals(gatewayRequest, save);
    }

    @Test
    void save_duplicatedSerial() {
        // mocked request
        GatewayRequest gatewayRequest = new GatewayRequest();
        gatewayRequest.setSerialNumber("serial9");
        gatewayRequest.setName("name9");
        gatewayRequest.setIpv4("192.168.1.9");
        // mocked saved data
        Gateway gateway = new Gateway();
        gateway.setId(9L);
        gateway.setSerialNumber("serial9");
        gateway.setName("name9");
        gateway.setIpv4("192.168.1.9");
        when(gatewayRepository.findBySerialNumber(gatewayRequest.getSerialNumber())).thenReturn(Optional.of(gateway));
        when(gatewayRepository.save(any())).thenReturn(gateway);
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> {
                    gateWayService.save(gatewayRequest);
                });
    }

    @Test
    void deleteBySerialNumber() {
        String serial = "serial1";
        // mocked data
        Gateway gateway = new Gateway();
        gateway.setId(9L);
        gateway.setSerialNumber("serial1");
        gateway.setName("name1");
        gateway.setIpv4("192.168.1.2");
        when(gatewayRepository.findBySerialNumber(serial)).thenReturn(Optional.of(gateway));
        assertThatCode(() -> gateWayService.deleteBySerialNumber(serial)).doesNotThrowAnyException();
    }

    @Test
    void deleteBySerialNumber_notfound() {
        String serial = "serial11";
        when(gatewayRepository.findBySerialNumber(serial)).thenReturn(Optional.empty());
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    gateWayService.deleteBySerialNumber(serial);
                });
    }

    @Test
    void update() {
        String serial = "serial9";
        // mocked request
        GatewayUpdateRequest gatewayRequest = new GatewayUpdateRequest();
        gatewayRequest.setName("name10");
        gatewayRequest.setIpv4("192.168.1.10");
        // mocked saved data
        Gateway gateway = new Gateway();
        gateway.setId(9L);
        gateway.setSerialNumber("serial9");
        gateway.setName("name9");
        gateway.setIpv4("192.168.1.9");
        when(gatewayRepository.findBySerialNumber(serial)).thenReturn(Optional.of(gateway));
        gateway.setName("name10");
        gateway.setIpv4("192.168.1.10");
        when(gatewayRepository.save(any())).thenReturn(gateway);
        GatewayResponse update = gateWayService.update(serial, gatewayRequest);
        testEquals(gateway, update);

    }

    @Test
    void update_notfound() {
        String serial = "serial9";
        // mocked request
        GatewayUpdateRequest gatewayRequest = new GatewayUpdateRequest();
        gatewayRequest.setName("name9");
        gatewayRequest.setIpv4("192.168.1.9");
        // mocked saved data
        Gateway gateway = new Gateway();
        gateway.setId(9L);
        gateway.setSerialNumber("serial9");
        gateway.setName("name9");
        gateway.setIpv4("192.168.1.9");
        when(gatewayRepository.findBySerialNumber(serial)).thenReturn(Optional.empty());
        when(gatewayRepository.save(any())).thenReturn(gateway);
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    gateWayService.update(serial, gatewayRequest);
                });
    }

    private void testEquals(Gateway actual, GatewayResponse expected) {
        assertThat(actual.getSerialNumber()).isEqualTo(expected.getSerialNumber());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getIpv4()).isEqualTo(expected.getIpv4());
    }

    private void testEquals(GatewayRequest actual, GatewayResponse expected) {
        assertThat(actual.getSerialNumber()).isEqualTo(expected.getSerialNumber());
        assertThat(actual.getName()).isEqualTo(expected.getName());
        assertThat(actual.getIpv4()).isEqualTo(expected.getIpv4());
    }
}
