package by.youngliqui.EShopProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Подробности заказа")
public class BrandDTO {
    @Schema(description = "Уникальный индентификатор бренда")
    private Long id;

    @Schema(description = "Название бренда")
    private String name;

    @Schema(description = "Описание бренда")
    private String descriptions;

    @Schema(description = "Продукты бренда")
    private List<ProductDTO> products;
}
