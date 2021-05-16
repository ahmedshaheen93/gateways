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
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class GateWayServiceImplTest {

    private TestHelper testHelper;

    @Autowired
    private GatewayRepository gatewayRepository;
    @Autowired
    private GatewayMapper gatewayMapper;

    private GateWayServiceImpl gateWayService;

    @BeforeEach
    public void setup() {
        testHelper = new TestHelper();
        gatewayRepository.saveAll(testHelper.gatewayList());
        gateWayService = new GateWayServiceImpl(gatewayRepository, gatewayMapper);
    }

    @AfterEach
    public void destroy() {
        gatewayRepository.deleteAll();
    }


    @Test
    void getAllGateways() {
        List<Gateway> gateways = testHelper.gatewayList();
        List<GatewayResponse> allGateways = gateWayService.getAllGateways();
        assertThat(allGateways.size()).isEqualTo(3);
        for (int i = 0; i < gateways.size(); i++) {
            testEquals(gateways.get(i), allGateways.get(i));
        }
    }

    @Test
    void getAllGateways_EmptyList() {
        gatewayRepository.deleteAll();
        List<GatewayResponse> allGateways = gateWayService.getAllGateways();
        assertThat(allGateways.size()).isEqualTo(0);
    }

    @Test
    void getGatewayBySerialNumber() {
        String serialNumber = "serial1";
        Optional<Gateway> first = testHelper.gatewayList().stream().findFirst();
        GatewayResponse gatewayBySerialNumber = gateWayService.getGatewayBySerialNumber(serialNumber);
        testEquals(first.get(), gatewayBySerialNumber);
    }

    @Test
    void getGatewayBySerialNumber_NotFoundException() {
        String serialNumber = "serial9";
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    gateWayService.getGatewayBySerialNumber(serialNumber);
                });
    }

    @Test
    void save() {
        GatewayRequest gatewayRequest = new GatewayRequest();
        gatewayRequest.setSerialNumber("serial9");
        gatewayRequest.setName("name9");
        gatewayRequest.setIpv4("192.168.1.9");
        GatewayResponse save = gateWayService.save(gatewayRequest);
        testEquals(gatewayRequest, save);
    }

    @Test
    void save_duplicatedSerial() {
        // mocked request
        GatewayRequest gatewayRequest = new GatewayRequest();
        gatewayRequest.setSerialNumber("serial1");
        gatewayRequest.setName("name9");
        gatewayRequest.setIpv4("192.168.1.9");
        assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> {
                    gateWayService.save(gatewayRequest);
                });
    }

    @Test
    void deleteBySerialNumber() {
        String serial = "serial1";
        assertThatCode(() -> gateWayService.deleteBySerialNumber(serial)).doesNotThrowAnyException();
    }

    @Test
    void deleteBySerialNumber_notfound() {
        String serial = "serial11";
        assertThatExceptionOfType(NotFoundException.class)
                .isThrownBy(() -> {
                    gateWayService.deleteBySerialNumber(serial);
                });
    }

    @Test
    void update() {
        String serial = "serial3";
        // mocked request
        GatewayUpdateRequest gatewayRequest = new GatewayUpdateRequest();
        gatewayRequest.setName("name10");
        gatewayRequest.setIpv4("192.168.1.10");
        GatewayResponse update = gateWayService.update(serial, gatewayRequest);
        Optional<Gateway> bySerialNumber = gatewayRepository.findBySerialNumber(serial);
        testEquals(bySerialNumber.get(), update);

    }

    @Test
    void update_notfound() {
        String serial = "serial9";
        // mocked request
        GatewayUpdateRequest gatewayRequest = new GatewayUpdateRequest();
        gatewayRequest.setName("name9");
        gatewayRequest.setIpv4("192.168.1.9");
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
