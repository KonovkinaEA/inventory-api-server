package com.example.inventoryapiserver.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Entity
public class Item extends BaseItem {

    public Item() {
        super();
    }

    public Item(UUID id, String name) {
        super(id, name);
    }
}
