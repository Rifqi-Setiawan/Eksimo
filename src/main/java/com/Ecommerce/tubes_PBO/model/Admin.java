package com.Ecommerce.tubes_PBO.model;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Admin extends User {
    public Admin() {
        this.setRole(com.Ecommerce.tubes_PBO.enums.UserRole.ADMIN);
    }
}