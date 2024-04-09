package by.youngliqui.EShopProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Категория товара")
public class CategoryDTO {
    @Schema(description = "Название категории")
    private String title;
}
