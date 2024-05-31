package com.example.inventoryapiserver.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class ItemsPatch {

    List<Item> items;
    List<UUID> idsToDelete;
}
