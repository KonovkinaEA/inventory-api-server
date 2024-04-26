package com.example.inventoryapiserver.repositories;

import com.example.inventoryapiserver.models.Item;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long> {
}
