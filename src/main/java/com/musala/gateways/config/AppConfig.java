package com.musala.gateways.config;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public ModelMapper gateWayModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        // https:stackoverflow.com/questions/56715759/lazy-initialization-exception-in-model-mapper/58042468#58042468
        modelMapper.getConfiguration()
                .setPropertyCondition(context ->
                        !(context.getSource() instanceof PersistentCollection));
        return modelMapper;
    }

    @Bean
    public ModelMapper peripheralDeviceModelMapper() {
        return new ModelMapper();
    }

}
