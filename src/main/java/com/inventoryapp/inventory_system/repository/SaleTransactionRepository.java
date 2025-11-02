package com.inventoryapp.inventory_system.repository;

import com.inventoryapp.inventory_system.model.SaleTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleTransactionRepository extends JpaRepository<SaleTransaction, Long> {
    // I can add custom methods here later e.g to fetch sales for a report
}
