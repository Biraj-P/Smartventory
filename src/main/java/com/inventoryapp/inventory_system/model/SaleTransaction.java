package com.inventoryapp.inventory_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleTransaction implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Use SKU for simple linking, avoiding complex JPA relationships in this stage
    private String ProductSku;

    private Integer quantitySold;

    // Store the price at the time of sale for financial accuracy
    private Double unitPrice;

    private Double totalSaleAmount;

    // Automatic timestamp of the transaction
    private LocalDateTime saleTimestamp = LocalDateTime.now();

    // Optional (but good practice): Add a serial version UID
    @Serial
    private static final long serialVersionUID = 1L;
}
