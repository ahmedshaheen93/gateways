package com.musala.gateways.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ModelMapper gateWayModelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ModelMapper peripheralDeviceModelMapper() {
        return new ModelMapper();
    }

}
