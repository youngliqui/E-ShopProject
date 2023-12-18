package by.youngliqui.EShopProject.repositories;

import by.youngliqui.EShopProject.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
