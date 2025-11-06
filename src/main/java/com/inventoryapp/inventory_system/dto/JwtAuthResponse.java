package com.inventoryapp.inventory_system.dto;

import lombok.Data;

@Data
public class JwtAuthResponse {
    private String token;
    // TODO- Add more fields like username, roles, etc

    public JwtAuthResponse(String token) {
        this.token = token;
    }
}
