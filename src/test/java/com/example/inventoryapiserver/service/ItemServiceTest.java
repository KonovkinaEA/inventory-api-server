package com.example.inventoryapiserver.service;

import com.example.inventoryapiserver.model.BaseItem;
import com.example.inventoryapiserver.model.Item;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    private HttpServletResponse response;
    @Mock
    private MultipartFile file;

    @InjectMocks
    private ItemService itemService;

    @Test
    public void testGetItemsFromExcel() {
        try (MockedStatic<ExcelService> excelService = mockStatic(ExcelService.class)) {
            List<Item> expectedList = Arrays.asList(mock(Item.class), mock(Item.class));
            excelService.when(() -> ExcelService.isValidExcelFile(any())).thenReturn(true);
            excelService.when(() -> ExcelService.getItemsDataFromExcel(any())).thenReturn(expectedList);

            List<Item> list = itemService.getItemsFromExcel(mock(MultipartFile.class));
            assertEquals(expectedList, list);
        }
    }

    @Test
    public void testGetItemsFromExcelThrowsException() throws IOException {
        try (MockedStatic<ExcelService> excelService = mockStatic(ExcelService.class)) {
            excelService.when(() -> ExcelService.isValidExcelFile(any())).thenReturn(true);
            when(file.getInputStream()).thenThrow(new IOException("Test Exception"));

            IllegalArgumentException exception =
                    assertThrows(IllegalArgumentException.class, () -> itemService.getItemsFromExcel(file));
            excelService.verify(() -> ExcelService.isValidExcelFile(file), times(1));
            assertEquals("Этот файл не является допустимым файлом Excel", exception.getMessage());
        }
    }

    @Test
    public void testGetItemsFromExcelWithInvalidFile() {
        try (MockedStatic<ExcelService> excelService = mockStatic(ExcelService.class)) {
            excelService.when(() -> ExcelService.isValidExcelFile(any())).thenReturn(false);

            List<Item> list = itemService.getItemsFromExcel(mock(MultipartFile.class));
            assertTrue(list.isEmpty());
        }
    }

    @Test
    public void testGetExcelFromItems() throws Exception {
        try (MockedStatic<ExcelService> excelService = mockStatic(ExcelService.class)) {
            excelService.when(() -> ExcelService.exportItemsDataToExcel(any(), anyList())).thenAnswer(invocation -> null);
            doNothing().when(response).setContentType(anyString());
            doNothing().when(response).setHeader(anyString(), anyString());
            List<BaseItem> list = Arrays.asList(mock(BaseItem.class), mock(BaseItem.class));

            itemService.getExcelFromItems(response, list);

            verify(response, times(1)).setContentType("application/octet-stream");
            verify(response, times(1))
                    .setHeader("Content-Disposition", "attachment;filename=Inventory.xls");
            excelService.verify(
                    () -> ExcelService.exportItemsDataToExcel(response, list),
                    times(1)
            );
        }
    }
}
