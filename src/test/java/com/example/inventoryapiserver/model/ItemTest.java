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
        assertTrue(item.getIsCorrectlyPlaced());
        assertNull(item.getCount());
        assertNotNull(item.getChangedAt());
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
        assertTrue(item.getIsCorrectlyPlaced());
        assertNull(item.getCount());
        assertNotNull(item.getChangedAt());
        assertEquals(0L, item.getRevision());
    }

    @Test
    public void testSettersAndGetters() {
        UUID id = UUID.randomUUID();
        String name = "Test Item";
        String code = "Test Code";
        String inventoryNum = "Test Inventory Number";
        String barcode = "Test Barcode";
        Long manufactureDate = System.currentTimeMillis();
        String factoryNum = "Test Factory Number";
        String building = "Test Building";
        String location = "Test Location";
        Boolean isCorrectlyPlaced = false;
        Integer count = 10;
        Long changedAt = System.currentTimeMillis();
        Long revision = 1L;

        Item item = new Item();
        item.setId(id);
        item.setName(name);
        item.setCode(code);
        item.setInventoryNum(inventoryNum);
        item.setBarcode(barcode);
        item.setManufactureDate(manufactureDate);
        item.setFactoryNum(factoryNum);
        item.setBuilding(building);
        item.setLocation(location);
        item.setIsCorrectlyPlaced(isCorrectlyPlaced);
        item.setCount(count);
        item.setChangedAt(changedAt);
        item.setRevision(revision);

        assertEquals(id, item.getId());
        assertEquals(name, item.getName());
        assertEquals(code, item.getCode());
        assertEquals(inventoryNum, item.getInventoryNum());
        assertEquals(barcode, item.getBarcode());
        assertEquals(manufactureDate, item.getManufactureDate());
        assertEquals(factoryNum, item.getFactoryNum());
        assertEquals(building, item.getBuilding());
        assertEquals(location, item.getLocation());
        assertFalse(item.getIsCorrectlyPlaced());
        assertEquals(count, item.getCount());
        assertEquals(changedAt, item.getChangedAt());
        assertEquals(revision, item.getRevision());
    }

    @Test
    public void testEqualsAndHashCode() {
        UUID id = UUID.randomUUID();
        String name = "Test Item";

        Item item1 = new Item(id, name);
        Item item2 = new Item(id, name);

        assertEquals(item1, item2);
        assertEquals(item1.hashCode(), item2.hashCode());

        item2.setName("Different Name");

        assertNotEquals(item1, item2);
        assertNotEquals(item1.hashCode(), item2.hashCode());
    }
}

