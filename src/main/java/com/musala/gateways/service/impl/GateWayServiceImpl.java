package com.musala.gateways.service.impl;

import com.musala.gateways.exception.BadRequestException;
import com.musala.gateways.exception.NotFoundException;
import com.musala.gateways.model.Gateway;
import com.musala.gateways.model.PeripheralDevice;
import com.musala.gateways.model.Status;
import com.musala.gateways.openapi.model.*;
import com.musala.gateways.repository.GatewayRepository;
import com.musala.gateways.service.GateWayService;
import com.musala.gateways.utils.GatewayMapper;
import com.musala.gateways.utils.PeripheralDeviceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GateWayServiceImpl implements GateWayService {

    private static final String GATEWAY_NOT_FOUND = "gateway with serial number '%s' was not found";
    private static final String GATEWAY_BAD_REQUEST = "gateway with serial number '%s' is already exists ";
    private static final String PERIPHERAL_DEVICE_NOT_FOUND = "Peripheral Device with uid '%s' attached to the Gateway with serial number '%s' was not found ";
    private static final String PERIPHERAL_DEVICE_EXCEEDED = "Number of Peripheral Devices exceeded";

    private final GatewayRepository gatewayRepository;
    private final GatewayMapper gatewayMapper;
    private final PeripheralDeviceMapper peripheralDeviceMapper;

    @Autowired
    public GateWayServiceImpl(GatewayRepository gatewayRepository, GatewayMapper gatewayMapper, PeripheralDeviceMapper peripheralDeviceMapper) {
        this.gatewayRepository = gatewayRepository;
        this.gatewayMapper = gatewayMapper;
        this.peripheralDeviceMapper = peripheralDeviceMapper;
    }

    @Override
    public List<GatewayResponse> getAllGateways() {
        return gatewayRepository.findAll().stream()
                .map(this::mapGatewayResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GatewayResponse getGatewayBySerialNumber(String serialNumber) {
        Optional<Gateway> optionalGateway = gatewayRepository.findBySerialNumber(serialNumber);
        if (optionalGateway.isPresent()) {
            return mapGatewayResponse(optionalGateway.get());
        }
        throw new NotFoundException(String.format(GATEWAY_NOT_FOUND, serialNumber));
    }

    @Override
    public GatewayResponse save(GatewayRequest gatewayRequest) {
        Optional<Gateway> bySerialNumber = gatewayRepository.findBySerialNumber(gatewayRequest.getSerialNumber());
        if (bySerialNumber.isPresent()) {
            throw new BadRequestException(String.format(GATEWAY_BAD_REQUEST, gatewayRequest.getSerialNumber()));
        }
        Gateway gateway = gatewayMapper.mapGateway(gatewayRequest);
        Gateway savedGateway = gatewayRepository.save(gateway);
        return gatewayMapper.map(savedGateway);
    }

    @Override
    public void deleteBySerialNumber(String serialNumber) {
        Optional<Gateway> bySerialNumber = gatewayRepository.findBySerialNumber(serialNumber);
        bySerialNumber.ifPresentOrElse(gatewayRepository::delete, () -> {
            throw new NotFoundException(String.format(GATEWAY_NOT_FOUND, serialNumber));
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
        throw new NotFoundException(String.format(GATEWAY_NOT_FOUND, serialNumber));
    }

    @Override
    public List<PeripheralResponse> getPeripheralDevices(String serialNumber) {
        Optional<Gateway> bySerialNumber = gatewayRepository.findBySerialNumber(serialNumber);
        if (bySerialNumber.isPresent()) {
            return bySerialNumber.get().getPeripheralDevices()
                    .stream().filter(Objects::nonNull)
                    .map(peripheralDeviceMapper::map).collect(Collectors.toList());
        }
        throw new NotFoundException(String.format(GATEWAY_NOT_FOUND, serialNumber));
    }

    @Override
    public PeripheralResponse addPeripheralDevice(String serialNumber, PeripheralRequest peripheralRequest) {
        Optional<Gateway> optionalGateway = gatewayRepository.findBySerialNumber(serialNumber);
        if (optionalGateway.isPresent()) {
            Gateway gateway = optionalGateway.get();
            int count = gateway.getPeripheralDevices().size();
            if (count < 10) {
                PeripheralDevice peripheralDevice = peripheralDeviceMapper.mapPeripheralDevice(peripheralRequest);
                gateway.getPeripheralDevices().add(peripheralDevice);
                gatewayRepository.save(gateway);
                return peripheralDeviceMapper.map(peripheralDevice);
            } else {
                throw new BadRequestException(PERIPHERAL_DEVICE_EXCEEDED);
            }
        }
        throw new NotFoundException(String.format(GATEWAY_NOT_FOUND, serialNumber));
    }


    @Override
    public PeripheralResponse getPeripheralDevicesBySerialNumberAndUid(String serialNumber, Long uid) {
        Optional<Gateway> optionalGateway = gatewayRepository.findBySerialNumber(serialNumber);
        if (optionalGateway.isPresent()) {
            Optional<PeripheralDevice> optionalPeripheralDevice = optionalGateway.get()
                    .getPeripheralDevices()
                    .stream()
                    .filter(peripheralDevice -> peripheralDevice.getUid().equals(uid))
                    .findFirst();
            if (optionalPeripheralDevice.isPresent()) {
                return peripheralDeviceMapper.map(optionalPeripheralDevice.get());
            } else {
                throw new NotFoundException(String.format(PERIPHERAL_DEVICE_NOT_FOUND, uid, serialNumber));
            }
        }

        throw new NotFoundException(String.format(GATEWAY_NOT_FOUND, serialNumber));
    }

    @Override
    public PeripheralResponse updatePeripheralDevice(String serialNumber, Long uid, PeripheralUpdateRequest peripheralUpdateRequest) {
        Optional<Gateway> optionalGateway = gatewayRepository.findBySerialNumber(serialNumber);
        if (optionalGateway.isPresent()) {
            Gateway gateway = optionalGateway.get();
            Optional<PeripheralDevice> optionalPeripheralDevice = gateway
                    .getPeripheralDevices()
                    .stream()
                    .filter(Objects::nonNull)
                    .filter(peripheralDevice -> peripheralDevice.getUid().equals(uid))
                    .findFirst();
            if (optionalPeripheralDevice.isPresent()) {
                PeripheralDevice peripheralDevice = optionalPeripheralDevice.get();
                peripheralDevice.setVendor(peripheralUpdateRequest.getVendor());
                peripheralDevice.setStatus(Status.getByString(peripheralUpdateRequest.getStatus().getValue()));
                peripheralDevice.setUid(uid);
                gatewayRepository.save(gateway);
                return peripheralDeviceMapper.map(peripheralDevice);
            }
        }
        throw new NotFoundException(String.format(PERIPHERAL_DEVICE_NOT_FOUND, uid, serialNumber));
    }

    @Override
    public void deletePeripheralDeviceBySerialNumberAndUid(String serialNumber, Long uid) {
        Optional<Gateway> optionalGateway = gatewayRepository.findBySerialNumber(serialNumber);
        optionalGateway.ifPresentOrElse(gateway -> {
                    gateway.getPeripheralDevices()
                            .removeIf(peripheralDevice -> peripheralDevice.getUid().equals(uid));
                    gatewayRepository.save(gateway);
                }, () -> {
                    throw new NotFoundException(String.format(GATEWAY_NOT_FOUND, serialNumber));
                }
        );
    }

    private GatewayResponse mapGatewayResponse(Gateway gateway) {
        GatewayResponse response = gatewayMapper.map(gateway);
        List<PeripheralResponse> devices = gateway.getPeripheralDevices().stream()
                .map(peripheralDeviceMapper::map).collect(Collectors.toList());
        response.setPeripheralDevices(devices);
        return response;
    }
}
