package com.musala.gateways.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "PERIPHERAL_DEVICES")
@Getter
@Setter
public class PeripheralDevice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "UID")
    private Long uid;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "CREATED_DATE")
    private LocalDate createdDate;

    @Column(name = "STATUS")
    private Status status;
    @ManyToOne
    private Gateway gateway;

}
