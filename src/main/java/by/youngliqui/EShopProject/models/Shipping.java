package by.youngliqui.EShopProject.models;

import by.youngliqui.EShopProject.models.Enums.ShippingType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "shipping's")
public class Shipping {
    private static final String SEQ_NAME = "shipping_seq";
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ_NAME)
    @SequenceGenerator(name = SEQ_NAME, sequenceName = SEQ_NAME, allocationSize = 1)
    private Long id;
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String shippingCompany;

    private String trackingNumber;

    private LocalDate shippingDate;

    private LocalDate deliveryDate;

    @Enumerated(EnumType.STRING)
    private ShippingType shippingType;

    private BigDecimal shippingCost;
}