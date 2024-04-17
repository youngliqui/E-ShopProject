package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.OrderDTO;
import by.youngliqui.EShopProject.dto.PaymentDTO;
import by.youngliqui.EShopProject.models.Order;

import java.util.List;

public interface OrderService {
    void saveOrder(Order order);

    List<OrderDTO> getOrdersByUsername(String username);

    List<OrderDTO> getOrdersByUserId(Long id);

    List<Order> getAllOrders();

    void updateOrderStatus(String status, Long orderId);

    void createOrderPayment(PaymentDTO paymentDTO, String username);
}
