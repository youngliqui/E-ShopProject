package by.youngliqui.EShopProject.repositories;

import by.youngliqui.EShopProject.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findFirstByTitleContainingIgnoreCase(String title);
}
