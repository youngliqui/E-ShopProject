package by.youngliqui.EShopProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

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

    @Schema(description = "Описание товара")
    private String description;
    @Schema(description = "Стоимость товара")
    private BigDecimal price;

    @Schema(description = "Скидка на товар")
    private BigDecimal sale;

    @Schema(description = "категории товара")
    private List<String> categoriesNames;

    @Schema(description = "бренд товара")
    private String brandName;

    @Schema(description = "цвет товара")
    private String color;

    @Schema(description = "Размер товара")
    private String size;

    @Schema(description = "Вес товара")
    private Float weight;

    @Schema(description = "Материал товара")
    private String material;

    @Schema(description = "Инструкция по уходу")
    private String careInstructions;

    @Schema(description = "Есть ли товар в наличии")
    private Boolean availability;

    @Schema(description = "Отзывы на товар")
    private List<ReviewsDTO> reviews;

    @Schema(description = "теги товара")
    private List<String> tags;
}
