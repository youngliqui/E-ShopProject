package by.youngliqui.EShopProject.repositories;

import by.youngliqui.EShopProject.models.Bucket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Long> {
}
