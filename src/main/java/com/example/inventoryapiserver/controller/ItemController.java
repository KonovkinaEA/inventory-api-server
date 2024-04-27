package com.example.inventoryapiserver.controller;

import com.example.inventoryapiserver.model.Item;
import com.example.inventoryapiserver.repository.ItemRepository;
import com.example.inventoryapiserver.service.ItemService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    public ItemController(ItemRepository itemRepository, ItemService itemService) {
        this.itemRepository = itemRepository;
        this.itemService = itemService;
    }

    @PostMapping("/upload")
    public List<Item> uploadItemsData(@RequestParam("file") MultipartFile file) {
        List<Item> items = itemService.saveItemsToDatabase(file);
        items = (List<Item>) itemRepository.saveAll(items);
        return items;
    }

    @GetMapping("")
    public List<Item> findAllItems() {
        List<Item> items = (List<Item>) itemRepository.findAll();
        items.sort(Comparator.comparing(Item::getName));
        return items;
    }

    @GetMapping("name/{name}")
    public List<Item> findItemsByName(@PathVariable("name") String name) {
        return itemRepository.findByName(name);
    }

    @GetMapping("location/{location}")
    public List<Item> findItemsByLocation(@PathVariable("location") String location) {
        return itemRepository.findByLocation(location);
    }

    @GetMapping("lastUpdatedBy/{lastUpdatedBy}")
    public List<Item> findItemsByLastUpdatedBy(@PathVariable("lastUpdatedBy") String lastUpdatedBy) {
        return itemRepository.findByLastUpdatedBy(lastUpdatedBy);
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

    @GetMapping("factoryNum/{factoryNum}")
    public Optional<Item> getItemByFactoryNum(@PathVariable("factoryNum") String factoryNum) {
        return itemRepository.findByFactoryNum(factoryNum);
    }

    @PostMapping("")
    public Item createItem(@RequestBody Item item) {
        Date date = new Date();

        Item newItem = new Item(item.getName());
        newItem.setCode(item.getCode());
        newItem.setInventoryNum(item.getInventoryNum());
        newItem.setBarcode(item.getBarcode());
        newItem.setManufactureDate(item.getManufactureDate());
        newItem.setFactoryNum(item.getFactoryNum());
        newItem.setBuilding(item.getBuilding());
        newItem.setLocation(item.getLocation());
        newItem.setCount(item.getCount());
        newItem.setChangedAt(date.getTime());
        newItem.setLastUpdatedBy(item.getLastUpdatedBy());

        newItem = itemRepository.save(newItem);

        return newItem;
    }

    @PutMapping("/{id}")
    public Item updateItem(@PathVariable("id") UUID id, @RequestBody Item item) {
        Item updatedItem = new Item(item.getName());

        Optional<Item> existingItem = itemRepository.findById(id);
        if (existingItem.isPresent()) {
            updatedItem = existingItem.get();
            if (item.getRevision() >= updatedItem.getRevision()) {
                Date date = new Date();

                updatedItem.setCode(item.getCode());
                updatedItem.setInventoryNum(item.getInventoryNum());
                updatedItem.setBarcode(item.getBarcode());
                updatedItem.setManufactureDate(item.getManufactureDate());
                updatedItem.setFactoryNum(item.getFactoryNum());
                updatedItem.setBuilding(item.getBuilding());
                updatedItem.setLocation(item.getLocation());
                updatedItem.setCount(item.getCount());
                updatedItem.setChangedAt(date.getTime());
                updatedItem.setLastUpdatedBy(item.getLastUpdatedBy());
                updatedItem.setRevision(item.getRevision() + 1);

                updatedItem = itemRepository.save(updatedItem);
            }
        }

        return updatedItem;
    }

    @DeleteMapping("/{id}")
    public void deleteItem(@PathVariable("id") UUID id) {
        Optional<Item> item = itemRepository.findById(id);
        item.ifPresent(itemRepository::delete);
    }
}
