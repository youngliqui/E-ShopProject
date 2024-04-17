package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.ReviewsDTO;
import by.youngliqui.EShopProject.exceptions.UserNotAuthorizeException;
import by.youngliqui.EShopProject.services.ReviewService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Отзывы", description = "методы для работы с отзывами")
@OpenAPIDefinition(info = @Info(title = "E-SHOP API", version = "v1"))
@SecurityRequirement(name = "basicAuth")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GetMapping("/{productId}")
    @Operation(summary = "получение всех отзывов товара по id")
    public List<ReviewsDTO> getProductReviewsById(@Parameter(description = "id товара")
                                                 @PathVariable("productId") Long productId) {

        return reviewService.getAllProductReviewsById(productId);
    }

    @GetMapping("title/{title}")
    @Operation(summary = "получение всех отзывов товара по названию")
    public List<ReviewsDTO> getProductReviewsById(@Parameter(description = "название товара")
                                                 @PathVariable("title") String title) {

        return reviewService.getAllProductReviewsByName(title);
    }

    @PostMapping("/{productId}")
    @Operation(summary = "добавление новых отзывов")
    public ResponseEntity<Void> addNewReviewToProductById(@PathVariable("productId") Long productId,
                                                          @RequestBody ReviewsDTO reviewsDTO,
                                                          Principal principal) {

        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        reviewService.addNewReviewToProductById(productId, reviewsDTO, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
