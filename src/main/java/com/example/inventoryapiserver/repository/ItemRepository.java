package com.example.inventoryapiserver.repository;

import com.example.inventoryapiserver.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends CrudRepository<Item, UUID> {

    @Query("FROM Item WHERE code = ?1")
    Optional<Item> findByCode(String code);

    @Query("FROM Item WHERE inventoryNum = ?1")
    Optional<Item> findByInventoryNum(String inventoryNum);

    @Query("FROM Item WHERE barcode = ?1")
    Optional<Item> findByBarcode(String barcode);

    @Query("FROM Item WHERE location = ?1 ORDER BY name ASC")
    List<Item> findByLocation(String location);
}
