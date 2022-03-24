package com.shaheen.gateways.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shaheen.gateways.openapi.model.GatewayRequest;
import com.shaheen.gateways.openapi.model.GatewayResponse;
import com.shaheen.gateways.openapi.model.GatewayUpdateRequest;
import com.shaheen.gateways.repository.GatewayRepository;
import com.shaheen.gateways.utils.TestHelper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.transaction.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
class GatewaysControllerTest {
    private static final String GATEWAYS = "/gateways";
    private static final String GATEWAYS_SERIAL_NUMBER = "/gateways/{serialNumber}";
    @Autowired
    private WebApplicationContext context;
    @Autowired
    private GatewayRepository gatewayRepository;

    private TestHelper testHelper;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        testHelper = new TestHelper();
        gatewayRepository.saveAll(testHelper.gatewayList());
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();

    }

    @AfterEach
    public void destroy() {
        gatewayRepository.deleteAll();
    }


    @Test
    void getAllGateways() throws Exception {
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
        gatewayRepository.deleteAll();
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
        gatewayRequest.setSerialNumber("serial8");
        gatewayRequest.setIpv4("192.168.1.2");
        mockMvc.perform(post(GATEWAYS)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gatewayRequest))
        )
                .andDo(print()).andExpect(status().isCreated())
                .andExpect(jsonPath("$.serialNumber").value("serial8"))
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
        mockMvc.perform(delete(GATEWAYS_SERIAL_NUMBER, serial)
                .accept(MediaType.APPLICATION_JSON)
        )
                .andDo(print()).andExpect(status().isNoContent());
    }

    @Test
    void deleteGateway_not_found() throws Exception {
        String serial = "serial8";
        mockMvc.perform(delete(GATEWAYS_SERIAL_NUMBER, serial)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isNotFound());
    }

    @Test
    void getGatewayBySerialNumber() throws Exception {
        String serial = "serial1";
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
        String serial = "serial8";
        GatewayUpdateRequest request = new GatewayUpdateRequest();
        request.setName("New Name");
        request.setIpv4("192.168.1.100");
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
