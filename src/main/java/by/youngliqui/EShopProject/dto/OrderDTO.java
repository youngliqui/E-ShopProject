package by.youngliqui.EShopProject.dto;

import by.youngliqui.EShopProject.models.OrderDetails;
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
@Schema(description = "Заказ")
public class OrderDTO {
    @Schema(description = "Имя заказчика")
    private String nameOfUser;

    @Schema(description = "Сумма заказа")
    private BigDecimal sum;

    @Schema(description = "Детали заказа")
    private List<OrderDetailsDTO> details;

    @Schema(description = "Статус заказа")
    private String status;
}
