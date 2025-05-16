package com.Ecommerce.tubes_PBO.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


@Entity
@Table(name = "users") 
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public String name;
    public String description;
    public int price;
    public String type;
    public int stock;
    public String size;

}
