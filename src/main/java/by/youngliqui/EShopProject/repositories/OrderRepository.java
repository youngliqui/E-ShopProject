package by.youngliqui.EShopProject.repositories;

import by.youngliqui.EShopProject.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser_NameIgnoreCase(String username);

    List<Order> findAllByUser_Id(Long userId);
}
