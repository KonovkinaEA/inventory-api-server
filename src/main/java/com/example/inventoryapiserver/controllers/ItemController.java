package com.example.inventoryapiserver.controllers;

import com.example.inventoryapiserver.models.Item;
import com.example.inventoryapiserver.repositories.ItemRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemRepository itemRepository;

    public ItemController(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @GetMapping("")
    public List<Item> findAllItems() {
        return (List<Item>) itemRepository.findAll();
    }

    @GetMapping("name/{name}")
    public List<Item> findItemsByName(@PathVariable("name") String name) {
        return itemRepository.findByName(name);
    }

    @GetMapping("location/{location}")
    public List<Item> findItemsByLocation(@PathVariable("location") String location) {
        return itemRepository.findByLocation(location);
    }

    @GetMapping("/{id}")
    public Optional<Item> getItem(@PathVariable("id") UUID id) {
        return itemRepository.findById(id);
    }

    @GetMapping("code/{code}")
    public Optional<Item> getItemByCode(@PathVariable("code") String code) {
        return itemRepository.findByCode(code);
    }

    @GetMapping("inventoryNum/{inventoryNum}")
    public Optional<Item> getItemByInventoryNum(@PathVariable("inventoryNum") String inventoryNum) {
        return itemRepository.findByInventoryNum(inventoryNum);
    }

    @GetMapping("barcode/{barcode}")
    public Optional<Item> getItemByBarcode(@PathVariable("barcode") Long barcode) {
        return itemRepository.findByBarcode(barcode);
    }

    @GetMapping("factoryNumber/{factoryNumber}")
    public Optional<Item> getItemByFactoryNumber(@PathVariable("factoryNumber") String factoryNumber) {
        return itemRepository.findByFactoryNumber(factoryNumber);
    }

    @PostMapping("")
    public Item createItem(@RequestBody Item item) {
        Item newItem = new Item();
        newItem.setCode(item.getCode());
        newItem.setName(item.getName());
        newItem.setInventoryNum(item.getInventoryNum());
        newItem.setBarcode(item.getBarcode());
        newItem.setManufactureDate(item.getManufactureDate());
        newItem.setFactoryNumber(item.getFactoryNumber());
        newItem.setUniversityBuilding(item.getUniversityBuilding());
        newItem.setLocation(item.getLocation());
        newItem.setCount(item.getCount());

        newItem = itemRepository.save(newItem);

        return newItem;
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable("id") UUID id, @RequestBody Item item) {
        Item updatedItem = new Item();

        Optional<Item> existingItem = itemRepository.findById(id);
        if (existingItem.isPresent()) {
            updatedItem = existingItem.get();
            updatedItem.setName(item.getName());
            updatedItem.setCode(item.getCode());
            updatedItem.setInventoryNum(item.getInventoryNum());
            updatedItem.setBarcode(item.getBarcode());
            updatedItem.setManufactureDate(item.getManufactureDate());
            updatedItem.setFactoryNumber(item.getFactoryNumber());
            updatedItem.setUniversityBuilding(item.getUniversityBuilding());
            updatedItem.setLocation(item.getLocation());
            updatedItem.setCount(item.getCount());

            updatedItem = itemRepository.save(updatedItem);
        }

        return updatedItem;
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") UUID id) {
        Optional<Item> item = itemRepository.findById(id);
        item.ifPresent(itemRepository::delete);
    }
}
