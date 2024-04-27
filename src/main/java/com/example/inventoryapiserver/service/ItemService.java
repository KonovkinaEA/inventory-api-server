package com.example.inventoryapiserver.service;

import com.example.inventoryapiserver.model.Item;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    public List<Item> saveItemsToDatabase(MultipartFile file) {
        List<Item> items = new ArrayList<>();

        if (ExcelUploadService.isValidExcelFile(file)) {
            try {
                items = ExcelUploadService.getItemsDataFromExcel(file.getInputStream());
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }

        return items;
    }
}
