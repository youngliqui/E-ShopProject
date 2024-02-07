package by.youngliqui.EShopProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BucketDTO {
    private int amountProducts;
    private Double sum;
    private List<BucketDetailsDTO> bucketDetails = new ArrayList<>();

    public void aggregate() {
        //! неверный подсчет кол-во продуктов, т.к мы считаем только кол-во разных, а одинаковых может быть несколько
        this.amountProducts = bucketDetails.size();
        this.sum = bucketDetails.stream()
                .map(BucketDetailsDTO::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
