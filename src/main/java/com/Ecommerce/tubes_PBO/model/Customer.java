package com.Ecommerce.tubes_PBO.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Customer extends User {
    @Column(unique = true)
    private String custId;
    private String name;
    private String address;
    private String phoneNumber;
}
