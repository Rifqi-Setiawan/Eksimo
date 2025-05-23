package com.Ecommerce.tubes_PBO.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@Setter
public class Review extends AuditAble {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private Integer rating;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(name = "review_date", nullable = false, updatable = false)
    private LocalDateTime reviewDate;

    @PrePersist
    protected void onPrePersist() {
        if (this.reviewDate == null) {
            this.reviewDate = LocalDateTime.now();
        }
    }
}