package com.example.inventoryapiserver;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class InventoryApiServerApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    public void mainLoads() {
        InventoryApiServerApplication.main(new String[]{});
    }
}
