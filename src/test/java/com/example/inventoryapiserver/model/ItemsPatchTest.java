package com.example.inventoryapiserver.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ItemsPatchTest {

    @Test
    void testGettersAndSetters() {
        ItemsPatch itemsPatch = new ItemsPatch();

        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(UUID.randomUUID(), "Item1"));
        itemList.add(new Item(UUID.randomUUID(), "Item2"));

        List<UUID> idsToDeleteList = new ArrayList<>();
        idsToDeleteList.add(UUID.randomUUID());
        idsToDeleteList.add(UUID.randomUUID());

        itemsPatch.setItems(itemList);
        itemsPatch.setIdsToDelete(idsToDeleteList);

        assertEquals(itemList, itemsPatch.getItems());
        assertEquals(idsToDeleteList, itemsPatch.getIdsToDelete());
    }

    @Test
    void testInitialState() {
        ItemsPatch itemsPatch = new ItemsPatch();

        assertNull(itemsPatch.getItems());
        assertNull(itemsPatch.getIdsToDelete());
    }

    @Test
    void testSetItems() {
        ItemsPatch itemsPatch = new ItemsPatch();
        List<Item> itemList = new ArrayList<>();
        itemList.add(new Item(UUID.randomUUID(), "Item1"));

        itemsPatch.setItems(itemList);

        assertEquals(itemList, itemsPatch.getItems());
    }

    @Test
    void testSetIdsToDelete() {
        ItemsPatch itemsPatch = new ItemsPatch();
        List<UUID> idsToDeleteList = new ArrayList<>();
        idsToDeleteList.add(UUID.randomUUID());

        itemsPatch.setIdsToDelete(idsToDeleteList);

        assertEquals(idsToDeleteList, itemsPatch.getIdsToDelete());
    }
}
