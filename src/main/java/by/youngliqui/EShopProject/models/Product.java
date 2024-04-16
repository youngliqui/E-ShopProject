package by.youngliqui.EShopProject.models;

import by.youngliqui.EShopProject.models.Enums.Size;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "products")
public class Product {
    private static final String SEQ_NAME = "product_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;
    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;
    private BigDecimal sale;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "products_categories",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;

    @Enumerated(EnumType.STRING)
    private Size size;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    private String color;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private Integer quantity;
    private Float weight;

    private String material;

    @Column(length = 500)
    private String careInstructions;

    private Boolean availability;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_tags",
            joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag_name")
    private List<String> tags;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;
    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private Payment payment;


    public void addCategory(Category category) {
        categories.add(category);
    }

    public void addBrand(Brand brand) {
        this.brand = brand;
    }

    public void addQuantity(Integer quantity) {
        this.quantity += quantity;
    }

    public void subtractQuantity(Integer quantity) {
        this.quantity -= quantity;
    }

    public void setAvailable() {
        availability = true;
    }

    public void setUnavailable() {
        availability = false;
    }
}
