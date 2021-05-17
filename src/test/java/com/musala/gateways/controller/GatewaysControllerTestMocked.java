package com.musala.gateways.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.musala.gateways.exception.BadRequestException;
import com.musala.gateways.exception.NotFoundException;
import com.musala.gateways.openapi.model.GatewayRequest;
import com.musala.gateways.openapi.model.GatewayResponse;
import com.musala.gateways.openapi.model.GatewayUpdateRequest;
import com.musala.gateways.service.GateWayService;
import com.musala.gateways.utils.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GatewaysController.class)
class GatewaysControllerTestMocked {
    private static final String GATEWAYS = "/gateways";
    private static final String GATEWAYS_SERIAL_NUMBER = "/gateways/{serialNumber}";
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private GateWayService gateWayService;

    private TestHelper testHelper;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        testHelper = new TestHelper();
    }

    @Test
    void getAllGateways() throws Exception {
        when(gateWayService.getAllGateways()).thenReturn(testHelper.mappedGateways());
        mockMvc.perform(get(GATEWAYS)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[*].serialNumber").isNotEmpty())
                .andExpect(jsonPath("$[*].name").isNotEmpty())
                .andExpect(jsonPath("$[*].ipv4").isNotEmpty());
    }

    @Test
    void getAllGateways_emptyBody() throws Exception {
        when(gateWayService.getAllGateways()).thenReturn(Collections.emptyList());
        mockMvc.perform(get(GATEWAYS)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void createGateway() throws Exception {
        GatewayRequest gatewayRequest = new GatewayRequest();
        gatewayRequest.setName("name1");
        gatewayRequest.setSerialNumber("serial1");
        gatewayRequest.setIpv4("192.168.1.2");
        when(gateWayService.save(any())).thenReturn(testHelper.mappedGateways().get(0));
        mockMvc.perform(post(GATEWAYS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gatewayRequest))
        )
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("serial1"))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.ipv4").value("192.168.1.2"));

    }

    @Test
    void createGateway_missing_body() throws Exception {
        mockMvc.perform(post(GATEWAYS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void createGateway_missing_required_jsonfield() throws Exception {
        GatewayRequest gatewayRequest = new GatewayRequest();
        gatewayRequest.setSerialNumber("serial1");
        gatewayRequest.setIpv4("192.168.1.2");
        mockMvc.perform(post(GATEWAYS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gatewayRequest))
        )
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void createGateway_invalid_ip_pattern() throws Exception {
        GatewayRequest gatewayRequest = new GatewayRequest();
        gatewayRequest.setName("name1");
        gatewayRequest.setSerialNumber("serial1");
        gatewayRequest.setIpv4("256.168.1.2");
        mockMvc.perform(post(GATEWAYS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gatewayRequest))
        )
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void createGateway_duplicated_gateway() throws Exception {
        GatewayRequest gatewayRequest = new GatewayRequest();
        gatewayRequest.setName("name1");
        gatewayRequest.setSerialNumber("serial1");
        gatewayRequest.setIpv4("254.168.1.2");

        when(gateWayService.save(any())).thenThrow(new BadRequestException(String.format("gateway with serial number '%s' is already exists ", gatewayRequest.getSerialNumber())));
        mockMvc.perform(post(GATEWAYS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gatewayRequest))
        )
                .andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    void deleteGateway() throws Exception {
        String serial = "serial1";
        doNothing().when(gateWayService).deleteBySerialNumber(serial);
        mockMvc.perform(delete(GATEWAYS_SERIAL_NUMBER, serial)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void deleteGateway_not_found() throws Exception {
        String serial = "serial8";
        doThrow(NotFoundException.class).when(gateWayService).deleteBySerialNumber(serial);
        mockMvc.perform(delete(GATEWAYS_SERIAL_NUMBER, serial)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void getGatewayBySerialNumber() throws Exception {
        String serial = "serial1";
        when(gateWayService.getGatewayBySerialNumber(serial)).thenReturn(testHelper.mappedGateways().get(0));
        mockMvc.perform(get(GATEWAYS_SERIAL_NUMBER, serial)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("serial1"))
                .andExpect(jsonPath("$.name").value("name1"))
                .andExpect(jsonPath("$.ipv4").value("192.168.1.2"));
    }

    @Test
    void getGatewayBySerialNumber_not_found() throws Exception {
        String serial = "serial8";
        when(gateWayService.getGatewayBySerialNumber(serial)).thenThrow(new NotFoundException(String.format("gateway with serial number '%s' was not found", serial)));

        doThrow(NotFoundException.class).when(gateWayService).deleteBySerialNumber(serial);
        mockMvc.perform(get(GATEWAYS_SERIAL_NUMBER, serial)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void updateGateway() throws Exception {
        String serial = "serial1";
        GatewayUpdateRequest request = new GatewayUpdateRequest();
        request.setName("New Name");
        request.setIpv4("192.168.1.100");

        GatewayResponse response = new GatewayResponse();
        response.setSerialNumber(serial);
        response.setName("New Name");
        response.setIpv4("192.168.1.100");

        when(gateWayService.update(serial, request)).thenReturn(response);
        mockMvc.perform(put(GATEWAYS_SERIAL_NUMBER, serial)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.serialNumber").value("serial1"))
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.ipv4").value("192.168.1.100"));
    }

    @Test
    void updateGateway_not_found() throws Exception {
        String serial = "serial1";
        GatewayUpdateRequest request = new GatewayUpdateRequest();
        request.setName("New Name");
        request.setIpv4("192.168.1.100");
        when(gateWayService.update(serial, request)).thenThrow(NotFoundException.class);
        mockMvc.perform(put(GATEWAYS_SERIAL_NUMBER, serial)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void updateGateway_missing_field() throws Exception {
        String serial = "serial1";
        GatewayUpdateRequest request = new GatewayUpdateRequest();
        request.setIpv4("192.168.1.100");
        mockMvc.perform(put(GATEWAYS_SERIAL_NUMBER, serial)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateGateway_empty_body() throws Exception {
        String serial = "serial1";
        mockMvc.perform(put(GATEWAYS_SERIAL_NUMBER, serial)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}