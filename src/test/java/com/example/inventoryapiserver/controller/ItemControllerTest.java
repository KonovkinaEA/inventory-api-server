package com.example.inventoryapiserver.controller;

import com.example.inventoryapiserver.model.Item;
import com.example.inventoryapiserver.model.ItemsPatch;
import com.example.inventoryapiserver.model.Report;
import com.example.inventoryapiserver.repository.ItemRepository;
import com.example.inventoryapiserver.repository.ReportRepository;
import com.example.inventoryapiserver.service.ItemService;
import com.example.inventoryapiserver.util.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRepository itemRepository;

    @MockBean
    private ReportRepository reportRepository;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<Item> items;
    private List<Report> reports;

    @BeforeEach
    public void setUp() throws Exception {
        Item item1 = new Item(UUID.randomUUID(), "Item 1");
        Item item2 = new Item(UUID.randomUUID(), "Item 2");
        Item item3 = new Item(UUID.randomUUID(), "Item 3");
        item1.setCode("Code 1");
        item1.setInventoryNum("Inventory Num 1");
        item1.setBarcode("Barcode 1");
        item2.setRevision(-1L);
        item2.setLocation("Location 1");
        item3.setLocation("Location 1");

        items = Arrays.asList(item1, item2, item3);
        reports = Utils.convertItemListToReportList(items);

        when(itemRepository.findAll()).thenReturn(items);
        when(itemRepository.findByLocation("Location 1")).thenReturn(Arrays.asList(item2, item3));
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.of(item1));
        when(itemRepository.findByCode(item1.getCode())).thenReturn(Optional.of(item1));
        when(itemRepository.findByInventoryNum(item1.getInventoryNum())).thenReturn(Optional.of(item1));
        when(itemRepository.findByBarcode(item1.getBarcode())).thenReturn(Optional.of(item1));
        when(itemRepository.save(any(Item.class))).thenReturn(item1);
        when(itemRepository.saveAll(items)).thenReturn(items);
        when(reportRepository.saveAll(any())).thenReturn(reports);
        doNothing().when(reportRepository).deleteAll();
        doNothing().when(itemService).getExcelFromItems(any(), anyList());
        when(itemService.getItemsFromExcel(any())).thenReturn(items);
    }

    @Test
    public void testFindItems() throws Exception {
        mockMvc.perform(get("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Item 1")))
                .andExpect(jsonPath("$[1].name", is("Item 2")))
                .andExpect(jsonPath("$[2].name", is("Item 3")));
    }

    @Test
    public void testFindItemsWithLocation() throws Exception {
        mockMvc.perform(get("/api/v1/items")
                        .param("location", "Location 1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Item 2")))
                .andExpect(jsonPath("$[1].name", is("Item 3")));
    }

    @Test
    public void testUpdateItems() throws Exception {
        ItemsPatch itemsPatch = new ItemsPatch();
        itemsPatch.setItems(items);
        itemsPatch.setIdsToDelete(List.of(UUID.randomUUID()));

        mockMvc.perform(patch("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsPatch)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Item 1")))
                .andExpect(jsonPath("$[1].name", is("Item 2")))
                .andExpect(jsonPath("$[2].name", is("Item 3")));
    }

    @Test
    public void testUpdateItemsWithLocation() throws Exception {
        ItemsPatch itemsPatch = new ItemsPatch();
        itemsPatch.setItems(items);
        itemsPatch.setIdsToDelete(List.of(UUID.randomUUID()));

        mockMvc.perform(patch("/api/v1/items")
                        .param("location", "Location 1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemsPatch)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Item 2")))
                .andExpect(jsonPath("$[1].name", is("Item 3")));
    }

    @Test
    public void testDeleteItems() throws Exception {
        mockMvc.perform(delete("/api/v1/items")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRepository, times(1)).deleteAll();
    }

    @Test
    public void testDownloadItems() throws Exception {
        mockMvc.perform(get("/api/v1/items/excel/download")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRepository, times(1)).findAll();
        verify(itemService, times(1)).getExcelFromItems(any(), anyList());
    }

    @Test
    public void testDownloadItemsWithLocation() throws Exception {
        mockMvc.perform(get("/api/v1/items/excel/download/3-405")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRepository, times(1)).findByLocation("3-405");
        verify(itemService, times(1)).getExcelFromItems(any(), anyList());
    }

    @Test
    public void testSaveReport() throws Exception {
        mockMvc.perform(post("/api/v1/items/excel/report")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(items)))
                .andExpect(status().isOk());

        verify(reportRepository, times(1)).deleteAll();
        verify(reportRepository, times(1)).saveAll(reports);
    }

    @Test
    public void testGetReport() throws Exception {
        mockMvc.perform(get("/api/v1/items/excel/report")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(reportRepository, times(1)).findAll();
        verify(itemService, times(1)).getExcelFromItems(any(), anyList());
        verify(reportRepository, times(1)).deleteAll();
    }

    @Test
    public void testUploadItems() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.xlsx",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                "mock content".getBytes()
        );

        mockMvc.perform(multipart("/api/v1/items/excel/upload")
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name", is("Item 1")))
                .andExpect(jsonPath("$[1].name", is("Item 2")))
                .andExpect(jsonPath("$[2].name", is("Item 3")));
    }

    @Test
    public void testFindItemById() throws Exception {
        mockMvc.perform(get("/api/v1/items/item")
                        .param("id", items.getFirst().getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Item 1")));
    }

    @Test
    public void testFindItemByCode() throws Exception {
        mockMvc.perform(get("/api/v1/items/item")
                        .param("code", items.getFirst().getCode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Item 1")));
    }

    @Test
    public void testFindItemByInventoryNum() throws Exception {
        mockMvc.perform(get("/api/v1/items/item")
                        .param("inventoryNum", items.getFirst().getInventoryNum())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Item 1")));
    }

    @Test
    public void testFindItemByBarcode() throws Exception {
        mockMvc.perform(get("/api/v1/items/item")
                        .param("barcode", items.getFirst().getBarcode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Item 1")));
    }

    @Test
    public void testFindItemTooManyParameters() throws Exception {
        mockMvc.perform(get("/api/v1/items/item")
                        .param("id", items.getFirst().getId().toString())
                        .param("code", items.getFirst().getCode())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Only one parameter must be specified"));
    }

    @Test
    public void testFindItemNoParameters() throws Exception {
        mockMvc.perform(get("/api/v1/items/item")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("At least one parameter must be specified"));
    }

    @Test
    public void testCreateItem() throws Exception {
        Item item = new Item(UUID.randomUUID(), "New Item");

        mockMvc.perform(post("/api/v1/items/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateItem() throws Exception {
        Item item = new Item(items.getFirst().getId(), "New Item 1");

        mockMvc.perform(put("/api/v1/items/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateItemEquals() throws Exception {
        mockMvc.perform(put("/api/v1/items/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(items.getFirst())))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateItemLowerRevision() throws Exception {
        Item item = new Item(items.getFirst().getId(), "Item 1");
        item.setRevision(-1L);

        mockMvc.perform(put("/api/v1/items/item")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteItem() throws Exception {
        mockMvc.perform(delete("/api/v1/items/item")
                        .param("id", items.getFirst().getId().toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(itemRepository, times(1)).deleteById(items.getFirst().getId());
    }
}
