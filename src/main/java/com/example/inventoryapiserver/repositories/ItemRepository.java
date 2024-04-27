package com.example.inventoryapiserver.repositories;

import com.example.inventoryapiserver.models.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ItemRepository extends CrudRepository<Item, UUID> {

    @Query("FROM Item WHERE barcode = ?1")
    Optional<Item> findByBarcode(Long barcode);

    @Query("FROM Item WHERE location = ?1")
    List<Item> findByLocation(String location);
}
