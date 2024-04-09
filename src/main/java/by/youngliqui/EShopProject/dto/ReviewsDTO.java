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
@Schema(description = "Информация об отзыве")
public class ReviewsDTO {
    @Schema(description = "Имя обозревателя")
    private String reviewerName;

    @Schema(description = "Отзыв")
    private String comment;

    @Schema(description = "Оценка")
    private int rating;
}
