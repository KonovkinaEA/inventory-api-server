package com.example.inventoryapiserver.config;

import com.example.inventoryapiserver.models.Item;
import com.example.inventoryapiserver.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Item> items = (List<Item>) itemRepository.findAll();

        if (items.isEmpty()) {
            Item item1 = new Item();
            Item item2 = new Item();

            item1.setName("This is the first item");
            item2.setName("This is the second item");

            itemRepository.save(item1);
            itemRepository.save(item2);
        }
    }
}
