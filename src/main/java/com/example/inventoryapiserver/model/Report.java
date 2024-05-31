package com.example.inventoryapiserver.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Report extends BaseItem {

    public Report() {
        super();
    }

    public Report(BaseItem baseItem) {
        super(baseItem.getId(), baseItem.getName());
        this.setCode(baseItem.getCode());
        this.setInventoryNum(baseItem.getInventoryNum());
        this.setBarcode(baseItem.getBarcode());
        this.setManufactureDate(baseItem.getManufactureDate());
        this.setFactoryNum(baseItem.getFactoryNum());
        this.setBuilding(baseItem.getBuilding());
        this.setLocation(baseItem.getLocation());
        this.setIsCorrectlyPlaced(baseItem.getIsCorrectlyPlaced());
        this.setCount(baseItem.getCount());
        this.setChangedAt(baseItem.getChangedAt());
        this.setRevision(baseItem.getRevision());
    }
}
