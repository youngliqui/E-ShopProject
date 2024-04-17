package by.youngliqui.EShopProject.servicesImpl;

import by.youngliqui.EShopProject.dto.ReviewsDTO;
import by.youngliqui.EShopProject.exceptions.ProductNotFoundException;
import by.youngliqui.EShopProject.exceptions.UserNotFoundException;
import by.youngliqui.EShopProject.models.Product;
import by.youngliqui.EShopProject.models.Review;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.repositories.ProductRepository;
import by.youngliqui.EShopProject.repositories.ReviewRepository;
import by.youngliqui.EShopProject.repositories.UserRepository;
import by.youngliqui.EShopProject.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static by.youngliqui.EShopProject.mappers.ReviewMapper.MAPPER;

@Service
@Transactional(readOnly = true)
public class ReviewServiceImpl implements ReviewService {
    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    @Autowired
    public ReviewServiceImpl(ProductRepository productRepository, ReviewRepository reviewRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<ReviewsDTO> getAllProductReviewsById(Long productId) {
        return MAPPER.fromReviewList(reviewRepository.findAllByProduct_Id(productId));
    }

    @Override
    public List<ReviewsDTO> getAllProductReviewsByName(String productName) {
        return MAPPER.fromReviewList(reviewRepository.findAllByProduct_TitleIgnoreCase(productName));
    }

    @Override
    @Transactional
    public void addNewReviewToProductById(Long productId, ReviewsDTO reviewsDTO, String reviewer) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("product with id " + productId + " was not found")
        );

        User user = userRepository.findFirstByName(reviewer);

        if (user == null) {
            throw new UserNotFoundException("user with name " + reviewer + " was not found");
        }

        Review review = Review.builder()
                .reviewer(user)
                .comment(reviewsDTO.getComment())
                .rating(reviewsDTO.getRating())
                .created(LocalDateTime.now())
                .product(product)
                .build();

        product.getReviews().add(review);

        reviewRepository.save(review);
    }
}
