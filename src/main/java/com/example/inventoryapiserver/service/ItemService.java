package com.example.inventoryapiserver.service;

import com.example.inventoryapiserver.model.Item;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    public List<Item> getItemsFromExcel(MultipartFile file) {
        List<Item> items = new ArrayList<>();

        if (ExcelService.isValidExcelFile(file)) {
            try {
                items = ExcelService.getItemsDataFromExcel(file.getInputStream());
            } catch (IOException e) {
                throw new IllegalArgumentException("The file is not a valid excel file");
            }
        }

        return items;
    }

    public void generateExcelReport(HttpServletResponse response, List<Item> items) throws Exception {
        response.setContentType("application/octet-stream");

        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=Inventory.xls";

        response.setHeader(headerKey, headerValue);

        ExcelService.exportItemsDataToExcel(response, items);
    }
}
