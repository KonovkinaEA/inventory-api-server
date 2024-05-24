package com.example.inventoryapiserver.repository;

import com.example.inventoryapiserver.model.Report;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportRepository extends CrudRepository<Report, UUID> {
}
