package by.youngliqui.EShopProject.repositories;

import by.youngliqui.EShopProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByName(String name);
}
