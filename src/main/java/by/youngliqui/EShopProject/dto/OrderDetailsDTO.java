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
@Schema(description = "Подробности заказа")
public class OrderDetailsDTO {
    @Schema(description = "Название товара")
    private String productName;
    @Schema(description = "Кол-во товаров")
    private BigDecimal amount;
    @Schema(description = "Цена одного товара")
    private BigDecimal price;
}
