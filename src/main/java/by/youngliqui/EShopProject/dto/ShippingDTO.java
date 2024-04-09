package by.youngliqui.EShopProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Доставка")
public class ShippingDTO {
    @Schema(description = "Название компании доставки")
    private String shippingCompany;
    @Schema(description = "Трек номер")
    private String trackingNumber;

    @Schema(description = "Дата отправки")
    private LocalDate shippingDate;

    @Schema(description = "Дата доставки")
    private LocalDate deliveryDate;

    @Schema(description = "Тип доставки")
    private String shippingType;

    @Schema(description = "Стоимость доставки")
    private BigDecimal shippingCost;
}
