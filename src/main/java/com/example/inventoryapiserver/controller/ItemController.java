package com.example.inventoryapiserver.controller;

import com.example.inventoryapiserver.model.Item;
import com.example.inventoryapiserver.repository.ItemRepository;
import com.example.inventoryapiserver.service.ItemService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;

    @GetMapping("/excel/download")
    public void downloadItemsData(HttpServletResponse response) throws Exception {
        List<Item> items = (List<Item>) itemRepository.findAll();
        items.sort(Comparator.comparing(item -> item.getName().toLowerCase()));
        itemService.generateExcelReport(response, items);
    }

    @PostMapping("/excel/upload")
    public List<Item> uploadItemsData(@RequestParam("file") MultipartFile file) {
        List<Item> items = itemService.getItemsFromExcel(file);
        items.sort(Comparator.comparing(item -> item.getName().toLowerCase()));
        items = (List<Item>) itemRepository.saveAll(items);

        return items;
    }

    @GetMapping("")
    public List<Item> findItems(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String lastUpdatedBy
    ) {
        int count = 0;
        if (StringUtils.hasText(name)) count++;
        if (StringUtils.hasText(location)) count++;
        if (StringUtils.hasText(lastUpdatedBy)) count++;
        if (count > 1) throw new IllegalArgumentException("Only one parameter must be specified");

        if (StringUtils.hasText(name)) return itemRepository.findByName(name);
        if (StringUtils.hasText(location)) return itemRepository.findByLocation(location);
        if (StringUtils.hasText(lastUpdatedBy)) return itemRepository.findByLastUpdatedBy(lastUpdatedBy);

        List<Item> items = (List<Item>) itemRepository.findAll();
        items.sort(Comparator.comparing(item -> item.getName().toLowerCase()));
        return items;
    }

    @GetMapping("item")
    public Optional<Item> getItem(
            @RequestParam(required = false) UUID id,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String inventoryNum,
            @RequestParam(required = false) Long barcode
    ) {
        int count = 0;
        if (id != null) count++;
        if (StringUtils.hasText(code)) count++;
        if (StringUtils.hasText(inventoryNum)) count++;
        if (barcode != null) count++;
        if (count > 1) throw new IllegalArgumentException("Only one parameter must be specified");

        if (id != null) return itemRepository.findById(id);
        if (StringUtils.hasText(code)) return itemRepository.findByCode(code);
        if (StringUtils.hasText(inventoryNum)) return itemRepository.findByInventoryNum(inventoryNum);
        if (barcode != null) return itemRepository.findByBarcode(barcode);

        throw new IllegalArgumentException("At least one parameter must be specified");
    }

    @PostMapping("item")
    public Item createItem(@RequestBody Item item) {
        Item newItem = new Item(item.getId(), item.getName());
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

    @PutMapping("item")
    public Item updateItem(@RequestBody Item item) {
        UUID id = item.getId();
        Item updatedItem = new Item();

        Optional<Item> existingItem = itemRepository.findById(id);
        if (existingItem.isPresent()) {
            updatedItem = existingItem.get();
            if (item.getRevision() >= updatedItem.getRevision()) {
                Date date = new Date();

                updatedItem.setName(item.getName());
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

    @DeleteMapping("item")
    public void deleteItem(@RequestParam UUID id) {
        itemRepository.deleteById(id);
    }
}
