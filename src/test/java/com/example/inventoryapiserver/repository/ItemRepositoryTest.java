package com.example.inventoryapiserver.repository;

import com.example.inventoryapiserver.model.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testFindByCode() {
        Item itemWithCode = new Item();
        itemWithCode.setCode("code");

        itemRepository.save(itemWithCode);

        Optional<Item> existingItem = itemRepository.findByCode("code");
        assertTrue(existingItem.isPresent());

        Item item = existingItem.get();
        assertEquals(itemWithCode, item);
    }

    @Test
    public void testFindByInventoryNum() {
        Item itemWithInventoryNum = new Item();
        itemWithInventoryNum.setInventoryNum("inventoryNum");

        itemRepository.save(itemWithInventoryNum);

        Optional<Item> existingItem = itemRepository.findByInventoryNum("inventoryNum");
        assertTrue(existingItem.isPresent());

        Item item = existingItem.get();
        assertEquals(itemWithInventoryNum, item);
    }

    @Test
    public void testFindByBarcode() {
        Item itemWithBarcode = new Item();
        itemWithBarcode.setBarcode("barcode");

        itemRepository.save(itemWithBarcode);

        Optional<Item> existingItem = itemRepository.findByBarcode("barcode");
        assertTrue(existingItem.isPresent());

        Item item = existingItem.get();
        assertEquals(itemWithBarcode, item);
    }

    @Test
    public void testFindByLocation() {
        Item item1 = new Item();
        Item item2 = new Item();
        item1.setLocation("location");
        item2.setLocation("location");

        itemRepository.saveAll(Arrays.asList(item1, item2));

        List<Item> list = itemRepository.findByLocation("location");
        assertEquals(list.size(), 2);
        assertTrue(list.contains(item1));
        assertTrue(list.contains(item2));
    }
}
