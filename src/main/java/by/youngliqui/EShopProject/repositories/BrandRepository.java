package by.youngliqui.EShopProject.repositories;

import by.youngliqui.EShopProject.models.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findFirstByNameContainingIgnoreCase(String name);

    Optional<Brand> findFirstByNameIgnoreCase(String name);

    Optional<Brand> findFirstByName(String name);
}
