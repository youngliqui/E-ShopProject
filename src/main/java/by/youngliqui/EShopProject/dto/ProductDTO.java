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
@Schema(description = "Информация о товаре")
public class ProductDTO {
    @Schema(description = "Уникальный идентификатор товара")
    private Long id;
    @Schema(description = "Название товара")
    private String title;
    @Schema(description = "Стоимость товара")
    private BigDecimal price;
}
