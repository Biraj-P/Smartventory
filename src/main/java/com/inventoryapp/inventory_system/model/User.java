package com.inventoryapp.inventory_system.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "app_user") // 'user' can be a reserved keyword in some DBs
@Data
public class User implements UserDetails, Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // This will be BCrypt-encoded password

    @Enumerated(EnumType.STRING) // Store enum as string for readability
    private Role role;

    // --- UserDetails Interface Methods ---
    // These methods tell Spring Security about the user's account status.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // This is the most important one: it tells Spring what role(s) the user has.
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    // For now, we assume all accounts are valid and active.(hardcore to true)
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

}
