package com.Ecommerce.tubes_PBO.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "customers")
public class Customer extends User {
    @Column(unique = true, nullable = false)
    private String custId;
    private String name;
    private String address;
    private String phoneNumber;
}
