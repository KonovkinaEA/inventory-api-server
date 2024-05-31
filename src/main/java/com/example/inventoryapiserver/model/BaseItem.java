package com.example.inventoryapiserver.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Setter
@Getter
@Entity
@EqualsAndHashCode
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class BaseItem implements Serializable {

    @Id
    @EqualsAndHashCode.Exclude
    @NotNull
    private UUID id;

    private String name;

    private String code;

    private String inventoryNum;

    private String barcode;

    private Long manufactureDate;

    private String factoryNum;

    private String building;

    private String location;

    private Boolean isCorrectlyPlaced;

    private Integer count;

    private Long changedAt;

    private Long revision;

    public BaseItem() {
        Date date = new Date();

        this.id = UUID.randomUUID();
        this.name = "";
        this.code = "";
        this.inventoryNum = "";
        this.barcode = "";
        this.factoryNum = "";
        this.building = "";
        this.location = "";
        this.isCorrectlyPlaced = true;
        this.changedAt = date.getTime();
        this.revision = 0L;
    }

    public BaseItem(UUID id, String name) {
        Date date = new Date();

        this.id = id;
        this.name = name;
        this.code = "";
        this.inventoryNum = "";
        this.barcode = "";
        this.factoryNum = "";
        this.building = "";
        this.location = "";
        this.isCorrectlyPlaced = true;
        this.changedAt = date.getTime();
        this.revision = 0L;
    }
}
