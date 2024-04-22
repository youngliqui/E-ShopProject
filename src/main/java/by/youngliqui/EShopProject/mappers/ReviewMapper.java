package by.youngliqui.EShopProject.mappers;

import by.youngliqui.EShopProject.dto.ReviewsDTO;
import by.youngliqui.EShopProject.models.Review;
import by.youngliqui.EShopProject.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ReviewMapper {
    ReviewMapper MAPPER = Mappers.getMapper(ReviewMapper.class);

    Review toReview(ReviewsDTO reviewsDTO);

    @Mapping(target = "reviewerName", source = "reviewer", qualifiedByName = "mapReviewerToReviewerName")
    ReviewsDTO fromReview(Review review);


    default List<ReviewsDTO> fromReviewList(List<Review> reviews) {
        return reviews.stream()
                .map(review -> ReviewsDTO.builder()
                        .reviewerName(review.getReviewer().getName())
                        .rating(review.getRating())
                        .comment(review.getComment())
                        .build()
                )
                .collect(Collectors.toList());
    }


    @Named("mapReviewerToReviewerName")
    default String mapReviewerToReviewerName(User reviewer) {
        return reviewer != null ? reviewer.getName() : null;
    }
}
