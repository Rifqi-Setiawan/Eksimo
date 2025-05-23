package com.Ecommerce.tubes_PBO.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product extends AuditAble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column
    private Integer price;

    @Column(nullable = false)
    private Integer stock;

    @ElementCollection 
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    private List<String> images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    @Column(name = "average_rating", columnDefinition = "DOUBLE PRECISION DEFAULT 0.0")
    private Double averageRating = 0.0;
}