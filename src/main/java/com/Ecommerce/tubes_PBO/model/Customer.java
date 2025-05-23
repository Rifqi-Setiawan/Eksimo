package com.Ecommerce.tubes_PBO.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@DiscriminatorValue("CUSTOMER") // Nilai untuk kolom user_type jika user adalah Customer
@Getter
@Setter
public class Customer extends User {

    @Column(name = "full_name")
    private String name; 

    @Column(name = "address")
    private String address; 

    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Cart cart;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Review> reviews;
    public Customer() {
        this.setRole(com.Ecommerce.tubes_PBO.enums.UserRole.CUSTOMER);
    }
}