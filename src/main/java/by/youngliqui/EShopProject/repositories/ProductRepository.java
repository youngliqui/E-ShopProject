package by.youngliqui.EShopProject.repositories;

import by.youngliqui.EShopProject.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByTitleContainingIgnoreCaseOrderByPriceAsc(String title, Pageable pageable);

    Page<Product> findAllByTitleContainingIgnoreCaseOrderByPriceDesc(String title, Pageable pageable);

    Page<Product> findAllByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.price ASC")
    Page<Product> findAllSortedByAscendingPrice(Pageable pageable);

    @Query("SELECT p FROM Product p ORDER BY p.price DESC")
    Page<Product> findAllSortedByDescendingPrice(Pageable pageable);
}
