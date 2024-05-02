package com.example.inventoryapiserver.controller;

import com.example.inventoryapiserver.model.Item;
import com.example.inventoryapiserver.repository.ItemRepository;
import com.example.inventoryapiserver.service.ItemService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    @GetMapping("/download")
    public void downloadItemsData(HttpServletResponse response) throws Exception {
        itemService.generateExcelReport(response, findAllItems());
    }

    @PostMapping("/upload")
    public List<Item> uploadItemsData(@RequestParam("file") MultipartFile file) {
        List<Item> items = itemService.getItemsFromExcel(file);
        items.sort(Comparator.comparing(item -> item.getName().toLowerCase()));
        itemRepository.deleteAll();
        items = (List<Item>) itemRepository.saveAll(items);
        return items;
    }

    @GetMapping("")
    public List<Item> findAllItems() {
        List<Item> items = (List<Item>) itemRepository.findAll();
        items.sort(Comparator.comparing(item -> item.getName().toLowerCase()));
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

    @PostMapping("")
    public Item createItem(@RequestBody Item item) {
        Item newItem = new Item(item.getName());
        newItem.setCode(item.getCode() != null ? item.getCode().trim() : null);
        newItem.setInventoryNum(item.getInventoryNum() != null ? item.getInventoryNum().trim() : null);
        newItem.setBarcode(item.getBarcode());
        newItem.setManufactureDate(item.getManufactureDate());
        newItem.setFactoryNum(item.getFactoryNum() != null ? item.getFactoryNum().trim() : null);
        newItem.setBuilding(item.getBuilding() != null ? item.getBuilding().trim() : null);
        newItem.setLocation(item.getLocation() != null ? item.getLocation().trim() : null);
        newItem.setCount(item.getCount());
        newItem.setLastUpdatedBy(item.getLastUpdatedBy() != null ? item.getLastUpdatedBy().trim() : null);

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

                updatedItem.setCode(item.getCode() != null ? item.getCode().trim() : null);
                updatedItem.setInventoryNum(item.getInventoryNum() != null ? item.getInventoryNum().trim() : null);
                updatedItem.setBarcode(item.getBarcode());
                updatedItem.setManufactureDate(item.getManufactureDate());
                updatedItem.setFactoryNum(item.getFactoryNum() != null ? item.getFactoryNum().trim() : null);
                updatedItem.setBuilding(item.getBuilding() != null ? item.getBuilding().trim() : null);
                updatedItem.setLocation(item.getLocation() != null ? item.getLocation().trim() : null);
                updatedItem.setCount(item.getCount());
                updatedItem.setChangedAt(date.getTime());
                updatedItem.setLastUpdatedBy(item.getLastUpdatedBy() != null ? item.getLastUpdatedBy().trim() : null);
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
