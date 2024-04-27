package com.example.inventoryapiserver.service;

import com.example.inventoryapiserver.model.Item;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ExcelUploadService {
    public static boolean isValidExcelFile(MultipartFile file) {
        return Objects.equals(file.getContentType(), "application/vnd.ms-excel");
    }

    public static List<Item> getItemsDataFromExcel(InputStream inputStream) {
        List<Item> items = new ArrayList<>();
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheet("Лист_1");

            int rowIndex = 0;
            for (Row row : sheet) {
                if (rowIndex <= 1) {
                    rowIndex++;
                    continue;
                }
                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;

                Item item = new Item();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    switch (cellIndex) {
                        case 1 -> item.setName(cell.getStringCellValue().trim());
                        case 2 -> item.setCode(cell.getStringCellValue().trim());
                        case 3 -> item.setInventoryNum(cell.getStringCellValue().trim());
                        case 4 -> item.setManufactureDate(convertToMilliseconds(cell.getStringCellValue()));
                        case 5 -> item.setFactoryNum(cell.getStringCellValue().trim());
                        case 6 -> item.setBuilding(cell.getStringCellValue().trim());
                        case 7 -> item.setLocation(cell.getStringCellValue().trim());
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

    private static long convertToMilliseconds(String dateStr) {
        long milliseconds = 0;

        if (!dateStr.isEmpty()) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                Date date = dateFormat.parse(dateStr);

                milliseconds = date.getTime();
            } catch (ParseException e) {
                System.err.println("Ошибка при преобразовании даты: " + e.getMessage());
            }
        }

        return milliseconds;
    }
}
