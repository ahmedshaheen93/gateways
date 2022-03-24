package com.shaheen.gateways.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "GATEWAY")
@Getter
@Setter
public class Gateway {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @NotBlank
    @Column(name = "SERIAL_NUMBER", unique = true)
    private String serialNumber;
    @NotBlank
    @Column(name = "NAME")
    private String name;
    @Pattern(regexp = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$")
    @Column(name = "IPV4")
    private String ipv4;

    @ElementCollection(fetch = FetchType.LAZY)
    @Size(max = 10)
    @JoinColumn(name = "GATEWAY_ID")
    private Set<PeripheralDevice> peripheralDevices = new HashSet<>();
}
