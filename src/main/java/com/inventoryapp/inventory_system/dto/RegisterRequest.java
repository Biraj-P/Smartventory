package com.inventoryapp.inventory_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    private String username;
    @NotBlank
    private String password;

    //We will default everyone to ROLE_USER for now.
    // TODO - Add a 'role' field here for admin registration
}
