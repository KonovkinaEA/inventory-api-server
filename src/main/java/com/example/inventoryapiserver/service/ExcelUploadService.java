package com.example.inventoryapiserver.service;

import com.example.inventoryapiserver.model.Item;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class ExcelUploadService {
    public static boolean isValidExcelFile(MultipartFile file) {
        System.out.println(file.getContentType());
        return Objects.equals(file.getContentType(), "application/vnd.ms-excel");
    }

    public static List<Item> getItemsDataFromExcel(InputStream inputStream) {
        List<Item> items = new ArrayList<>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet("items");

            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex == 0) {
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;

                Item item = new Item();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 1 -> item.setName(cell.getStringCellValue());
                        case 2 -> item.setCode(cell.getStringCellValue());
                        case 3 -> item.setInventoryNum(cell.getStringCellValue());
//                        case 4 -> item.setManufactureDate((long) cell.getNumericCellValue());
                        case 5 -> item.setFactoryNum(cell.getStringCellValue());
                        case 6 -> item.setBuilding(cell.getStringCellValue());
                        case 7 -> item.setLocation(cell.getStringCellValue());
                        case 8 -> item.setCount((int) cell.getNumericCellValue());
                        default -> {
                        }
                    }
                    cellIndex++;
                }
                items.add(item);
            }

            workbook.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return items;
    }

}
