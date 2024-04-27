package com.example.inventoryapiserver.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
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

    private String factoryNumber;

    private String universityBuilding;

    private String location;

    private Integer count;

    public Item() {
    }
}
