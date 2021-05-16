package com.musala.gateways.service.impl;

import com.musala.gateways.exception.BadRequestException;
import com.musala.gateways.exception.NotFoundException;
import com.musala.gateways.model.Gateway;
import com.musala.gateways.openapi.model.GatewayRequest;
import com.musala.gateways.openapi.model.GatewayResponse;
import com.musala.gateways.openapi.model.GatewayUpdateRequest;
import com.musala.gateways.repository.GatewayRepository;
import com.musala.gateways.service.GateWayService;
import com.musala.gateways.utils.GatewayMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GateWayServiceImpl implements GateWayService {
    private final GatewayRepository gatewayRepository;
    private final GatewayMapper gatewayMapper;

    @Autowired
    public GateWayServiceImpl(GatewayRepository gatewayRepository, GatewayMapper gatewayMapper) {
        this.gatewayRepository = gatewayRepository;
        this.gatewayMapper = gatewayMapper;
    }

    @Override
    public List<GatewayResponse> getAllGateways() {
        return gatewayRepository.findAll().stream()
                .map(gatewayMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public GatewayResponse getGatewayBySerialNumber(String serialNumber) {
        Optional<Gateway> bySerialNumber = gatewayRepository.findBySerialNumber(serialNumber);
        if (bySerialNumber.isPresent()) {
            return gatewayMapper.map(bySerialNumber.get());
        }
        throw new NotFoundException(String.format("gateway with serial number '%s' was not found", serialNumber));
    }

    @Override
    public GatewayResponse save(GatewayRequest gatewayRequest) {
        Optional<Gateway> bySerialNumber = gatewayRepository.findBySerialNumber(gatewayRequest.getSerialNumber());
        if (bySerialNumber.isPresent()) {
            throw new BadRequestException(String.format("gateway with serial number '%s' is already exists ", gatewayRequest.getSerialNumber()));
        }
        Gateway gateway = gatewayMapper.mapGateway(gatewayRequest);
        Gateway savedGateway = gatewayRepository.save(gateway);
        return gatewayMapper.map(savedGateway);
    }

    @Override
    public void deleteBySerialNumber(String serialNumber) {
        Optional<Gateway> bySerialNumber = gatewayRepository.findBySerialNumber(serialNumber);
        bySerialNumber.ifPresentOrElse(gatewayRepository::delete, () -> {
            throw new NotFoundException(String.format("gateway with serial number '%s' was not found", serialNumber));
        });
    }

    @Override
    public GatewayResponse update(String serialNumber, GatewayUpdateRequest gatewayRequest) {
        Optional<Gateway> bySerialNumber = gatewayRepository.findBySerialNumber(serialNumber);
        if (bySerialNumber.isPresent()) {
            Gateway gateway = bySerialNumber.get();
            gateway.setName(gatewayRequest.getName());
            gateway.setIpv4(gatewayRequest.getIpv4());
            Gateway save = gatewayRepository.save(gateway);
            return gatewayMapper.map(save);
        }
        throw new NotFoundException(String.format("gateway with serial number '%s' was not found", serialNumber));
    }
}
