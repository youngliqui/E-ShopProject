package by.youngliqui.EShopProject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class BucketDTO {
    private int amountProducts;
    private Double sum;
    private List<BucketDetailsDTO> bucketDetails = new ArrayList<>();

    public void aggregate() {
        this.amountProducts = bucketDetails.size();
        this.sum = bucketDetails.stream()
                .map(BucketDetailsDTO::getSum)
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}
