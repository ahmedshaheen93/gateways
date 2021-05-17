package com.musala.gateways.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;
import java.time.LocalDate;

@Embeddable
@Table(name = "PERIPHERAL_DEVICE")
@Getter
@Setter
public class PeripheralDevice {

    @Column(name = "UID")
    private Long uid;

    @Column(name = "VENDOR")
    private String vendor;

    @Column(name = "CREATED_DATE")
    @CreatedDate
    private LocalDate createdDate;

    @Column(name = "STATUS")
    private Status status;

}
