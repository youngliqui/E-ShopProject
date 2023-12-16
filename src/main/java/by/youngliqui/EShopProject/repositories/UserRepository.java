package by.youngliqui.EShopProject.repositories;

import by.youngliqui.EShopProject.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findFirstByName(String name);
}
