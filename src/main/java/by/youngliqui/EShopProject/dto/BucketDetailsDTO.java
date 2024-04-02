package by.youngliqui.EShopProject.dto;

import by.youngliqui.EShopProject.models.Product;
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
public class BucketDetailsDTO {
    @Schema(description = "название товара")
    private String title;
    //private Long productId;
    @Schema(description = "цена одного товара")
    private BigDecimal price;
    @Schema(description = "общая стоимость товаров")
    private BigDecimal amount;
    @Schema(description = "сумма товаров")
    private Double sum;

    public BucketDetailsDTO(Product product) {
        this.title = product.getTitle();
        this.amount = new BigDecimal("1.0");
        this.sum = Double.valueOf(product.getPrice().toString());
        //this.productId = product.getId();
        this.price = product.getPrice();
    }
}
