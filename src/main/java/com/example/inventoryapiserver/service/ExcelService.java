package com.example.inventoryapiserver.service;

import com.example.inventoryapiserver.model.BaseItem;
import com.example.inventoryapiserver.model.Item;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import static com.example.inventoryapiserver.util.Utils.*;

public class ExcelService {

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
                        case 9 -> item.setIsCorrectlyPlaced(!Objects.equals(cell.getStringCellValue(), "Нет"));
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

    public static void exportItemsDataToExcel(HttpServletResponse response, List<BaseItem> items) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Лист_1");

        HSSFRow title = sheet.createRow(0);
        title.createCell(0).setCellValue("ЦМО");
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 9));

        HSSFRow headlines = sheet.createRow(1);

        int rowIndex = 0;
        int[] maxColumnLengths = new int[HEADERS.length];
        for (String header : HEADERS) {
            headlines.createCell(rowIndex).setCellValue(header);
            maxColumnLengths[rowIndex] = header.length();

            rowIndex++;
        }

        rowIndex = 2;
        for (BaseItem item : items) {

            HSSFRow row = sheet.createRow(rowIndex);
            String[] values = {
                    String.valueOf(rowIndex - 1),
                    item.getName(),
                    item.getCode(),
                    item.getInventoryNum(),
                    convertToDate(item.getManufactureDate()),
                    item.getFactoryNum(),
                    item.getBuilding().trim(),
                    item.getLocation(),
            };

            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                row.createCell(i).setCellValue(value);
                if (value.length() > maxColumnLengths[i]) {
                    maxColumnLengths[i] = value.length();
                }
            }
            row.createCell(8).setCellValue(item.getCount());
            row.createCell(9).setCellValue(item.getIsCorrectlyPlaced() ? "Да" : "Нет");

            rowIndex++;
        }

        for (int i = 0; i < maxColumnLengths.length; i++) {
            sheet.setColumnWidth(i, maxColumnLengths[i] * 256 + 100);
        }

        ServletOutputStream ops = response.getOutputStream();
        workbook.write(ops);
        workbook.close();
        ops.close();
    }
}
