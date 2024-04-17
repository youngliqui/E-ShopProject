package by.youngliqui.EShopProject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Корзина заказов")
public class BucketDTO {
    @Schema(description = "кол-во продуктов")
    private int amountProducts;
    @Schema(description = "сумма товаров")
    private Double sum;
    @Schema(description = "Подробности заказа")
    private List<BucketDetailsDTO> bucketDetails = new ArrayList<>();

    public void aggregate() {
        this.amountProducts = bucketDetails.size();
        this.sum = bucketDetails.stream()
                .map(BucketDetailsDTO::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}

