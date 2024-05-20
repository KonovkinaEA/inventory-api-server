package com.example.inventoryapiserver.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {

    @Test
    public void testEmptyConstructor() {
        Item item = new Item();

        assertNotNull(item.getId());
        assertEquals("", item.getName());
        assertEquals("", item.getCode());
        assertEquals("", item.getInventoryNum());
        assertEquals("", item.getBarcode());
        assertNull(item.getManufactureDate());
        assertEquals("", item.getFactoryNum());
        assertEquals("", item.getBuilding());
        assertEquals("", item.getLocation());
        assertNull(item.getCount());
        assertNotNull(item.getChangedAt());
        assertEquals("", item.getLastUpdatedBy());
        assertEquals(0L, item.getRevision());
    }

    @Test
    public void testParameterizedConstructor() {
        String name = "Test Item";
        UUID id = UUID.randomUUID();

        Item item = new Item(id, name);

        assertEquals(id, item.getId());
        assertEquals(name, item.getName());
        assertEquals("", item.getCode());
        assertEquals("", item.getInventoryNum());
        assertEquals("", item.getBarcode());
        assertNull(item.getManufactureDate());
        assertEquals("", item.getFactoryNum());
        assertEquals("", item.getBuilding());
        assertEquals("", item.getLocation());
        assertNull(item.getCount());
        assertNotNull(item.getChangedAt());
        assertEquals("", item.getLastUpdatedBy());
        assertEquals(0L, item.getRevision());
    }
}
