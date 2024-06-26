package by.youngliqui.EShopProject.dto;

import by.youngliqui.EShopProject.models.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Подробности корзины")
public class BucketDetailsDTO {
    @Schema(description = "название товара")
    private String title;
    @Schema(description = "цена одного товара")
    private BigDecimal price;
    @Schema(description = "количество товара")
    private BigDecimal amount;
    @Schema(description = "сумма товаров")
    private Double sum;

    public BucketDetailsDTO(Product product) {
        this.title = product.getTitle();
        this.amount = new BigDecimal("1.0");
        this.price = product.getPrice().subtract(product.getPrice()
                        .multiply(product.getSale().multiply(BigDecimal.valueOf(0.01))))
                .setScale(2, RoundingMode.HALF_EVEN);
        this.sum = price.multiply(amount).doubleValue();
    }
}
