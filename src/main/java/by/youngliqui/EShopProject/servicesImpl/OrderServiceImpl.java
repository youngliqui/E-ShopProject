package by.youngliqui.EShopProject.servicesImpl;

import by.youngliqui.EShopProject.models.Order;
import by.youngliqui.EShopProject.repositories.OrderRepository;
import by.youngliqui.EShopProject.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
}
