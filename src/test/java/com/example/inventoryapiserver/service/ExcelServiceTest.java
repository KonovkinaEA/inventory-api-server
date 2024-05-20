package com.example.inventoryapiserver.service;

import com.example.inventoryapiserver.model.Item;
import com.example.inventoryapiserver.util.Utils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ExcelServiceTest {

    @Test
    public void testIsValidExcelFileTrue() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/vnd.ms-excel");

        assertTrue(ExcelService.isValidExcelFile(file));
    }

    @Test
    public void testIsValidExcelFileFalse() {
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("application/vnd.excel");

        assertFalse(ExcelService.isValidExcelFile(file));
    }

    @Test
    public void testGetItemsDataFromExcel() throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("Лист_1");

        Row headerRow = sheet.createRow(0);
        Row subHeaderRow = sheet.createRow(1);

        Row row = sheet.createRow(2);
        row.createCell(0).setCellValue(1);
        row.createCell(1).setCellValue("Item Name");
        row.createCell(2).setCellValue("Item Code");
        row.createCell(3).setCellValue("12345");
        row.createCell(4).setCellValue("01.12.2016");
        row.createCell(5).setCellValue("Factory Num");
        row.createCell(6).setCellValue("Building");
        row.createCell(7).setCellValue("Location");
        row.createCell(8).setCellValue(10);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        workbook.close();
        outputStream.close();

        try (MockedStatic<Utils> utils = mockStatic(Utils.class)) {
            utils.when(() -> Utils.convertToMilliseconds(anyString())).thenReturn(10L);
            List<Item> items = ExcelService.getItemsDataFromExcel(inputStream);

            assertNotNull(items);
            assertEquals(1, items.size());
            Item item = items.getFirst();
            assertEquals("Item Name", item.getName());
            assertEquals("Item Code", item.getCode());
            assertEquals("12345", item.getInventoryNum());
            assertNotNull(item.getManufactureDate());
            assertEquals("Factory Num", item.getFactoryNum());
            assertEquals("Building", item.getBuilding());
            assertEquals("Location", item.getLocation());
            assertEquals(10, item.getCount());
        }
    }

    @Test
    public void testGetItemsDataFromExcelThrowsException() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenThrow(new IOException("Test Exception"));

        List<Item> items = ExcelService.getItemsDataFromExcel(inputStream);

        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    @Test
    public void testExportItemsDataToExcel() throws Exception {
        Item item = new Item(UUID.randomUUID(), "Test Item");
        item.setCode("Code123");
        item.setInventoryNum("Inv123");
        item.setManufactureDate(System.currentTimeMillis());
        item.setFactoryNum("Factory123");
        item.setBuilding("Building123");
        item.setLocation("Location123");
        item.setCount(10);
        List<Item> items = List.of(item);

        HttpServletResponse response = mock(HttpServletResponse.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new DelegatingServletOutputStream(byteArrayOutputStream));

        ExcelService.exportItemsDataToExcel(response, items);

        verify(response).getOutputStream();

        try (Workbook workbook = new HSSFWorkbook(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()))) {
            Sheet sheet = workbook.getSheet("Лист_1");
            assertEquals("ЦМО", sheet.getRow(0).getCell(0).getStringCellValue());
            assertEquals("Test Item", sheet.getRow(2).getCell(1).getStringCellValue());
            assertEquals("Code123", sheet.getRow(2).getCell(2).getStringCellValue());
            assertEquals("Inv123", sheet.getRow(2).getCell(3).getStringCellValue());
            assertEquals("Factory123", sheet.getRow(2).getCell(5).getStringCellValue());
            assertEquals("Building123", sheet.getRow(2).getCell(6).getStringCellValue());
            assertEquals("Location123", sheet.getRow(2).getCell(7).getStringCellValue());
            assertEquals(10, (int) sheet.getRow(2).getCell(8).getNumericCellValue());
        }
    }

    private static class DelegatingServletOutputStream extends ServletOutputStream {
        private final OutputStream targetStream;

        public DelegatingServletOutputStream(OutputStream targetStream) {
            this.targetStream = targetStream;
        }

        @Override
        public void write(int b) {
            try {
                targetStream.write(b);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean isReady() {
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }
    }
}
