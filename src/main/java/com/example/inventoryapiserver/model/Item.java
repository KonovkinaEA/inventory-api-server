package com.example.inventoryapiserver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Entity
@ToString
@EqualsAndHashCode
public class Item implements Serializable {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Exclude
    private UUID id;

    private String name;

    private String code;

    private String inventoryNum;

    private Long barcode;

    private Long manufactureDate;

    private String factoryNum;

    private String building;

    private String location;

    private Integer count;

    private Long changedAt;

    private String lastUpdatedBy;

    private Long revision;

    public Item() {
        Date date = new Date();

        this.changedAt = date.getTime();
        this.revision = 0L;
    }

    public Item(String name) {
        Date date = new Date();

        this.name = name;
        this.changedAt = date.getTime();
        this.revision = 0L;
    }
}
