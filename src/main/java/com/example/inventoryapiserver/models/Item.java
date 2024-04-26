package com.example.inventoryapiserver.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@Entity
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

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

    public Item(String name, String code, Integer count) {
        this.name = name;
        this.code = code;
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format(
                "TodoItem{id=%d, name='%s', code='%s', inventoryNum='%s', barcode=%d, manufactureDate=%d, " +
                        "factoryNumber='%s', universityBuilding='%s', location='%s', count=%d}",
                id, name, code, inventoryNum, barcode, manufactureDate, factoryNumber,
                universityBuilding, location, count
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Item item)) return false;

        return Objects.equals(name, item.name) &&
                Objects.equals(code, item.code) &&
                Objects.equals(inventoryNum, item.inventoryNum) &&
                Objects.equals(barcode, item.barcode) &&
                Objects.equals(manufactureDate, item.manufactureDate) &&
                Objects.equals(factoryNumber, item.factoryNumber) &&
                Objects.equals(universityBuilding, item.universityBuilding) &&
                Objects.equals(location, item.location) &&
                Objects.equals(count, item.count);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code, inventoryNum, barcode, manufactureDate, factoryNumber,
                universityBuilding, location, count);
    }
}
