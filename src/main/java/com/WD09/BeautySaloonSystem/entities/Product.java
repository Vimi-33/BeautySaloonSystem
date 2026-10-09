package com.WD09.BeautySaloonSystem.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "product_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String brand;

    @Column(length = 500)
    private String description;

    private BigDecimal price;
    private int quantity;

    @Column(name = "img_url")
    private String imageUrl;

    // ---- abstract methods: every subclass MUST implement ----
    public abstract String getCategoryLabel();
    public abstract String getTypeCode();
    protected abstract BigDecimal getTaxRate();

    // ---- shared behaviour, uses polymorphic getTaxRate() ----
    public BigDecimal getFinalPrice() {
        return price.multiply(BigDecimal.ONE.add(getTaxRate()))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public boolean isAvailable() {
        return quantity > 0;
    }

    // ---- getters / setters with validation (encapsulation) ----
    public Long getId() { return id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Product name is required");
        this.name = name.trim();
    }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) {
        if (price == null || price.signum() < 0)
            throw new IllegalArgumentException("Price cannot be negative");
        this.price = price;
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        if (quantity < 0)
            throw new IllegalArgumentException("Quantity cannot be negative");
        this.quantity = quantity;
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}