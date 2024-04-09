package by.youngliqui.EShopProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Оплата заказа")
public class PaymentDTO {
    @Schema(description = "Тип оплаты")
    private String paymentType;

    @Schema(description = "Сумма оплаты")
    private BigDecimal amount;
}
