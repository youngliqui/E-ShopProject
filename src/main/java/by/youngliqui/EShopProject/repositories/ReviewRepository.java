package by.youngliqui.EShopProject.repositories;

import by.youngliqui.EShopProject.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByProduct_Id(Long productId);

    List<Review> findAllByProduct_TitleIgnoreCase(String title);
}
