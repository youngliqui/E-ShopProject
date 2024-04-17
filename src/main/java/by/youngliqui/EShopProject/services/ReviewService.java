package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.ReviewsDTO;

import java.util.List;

public interface ReviewService {
    List<ReviewsDTO> getAllProductReviewsById(Long productId);

    List<ReviewsDTO> getAllProductReviewsByName(String productName);

    void addNewReviewToProductById(Long productId, ReviewsDTO reviewsDTO, String reviewer);
}
