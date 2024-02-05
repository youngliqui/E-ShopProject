package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.models.Order;
import by.youngliqui.EShopProject.models.OrderDetails;
import by.youngliqui.EShopProject.models.OrderStatus;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.repositories.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void saveOrder() {
        Order savedOrder = Order.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .updated(LocalDateTime.now())
                .user(User.builder().id(100L).build())
                .sum(BigDecimal.valueOf(100))
                .address("address")
                .details(Collections.singletonList(OrderDetails.builder().id(100L).build()))
                .status(OrderStatus.NEW)
                .build();

        orderService.saveOrder(savedOrder);

        verify(orderRepository).save(Mockito.eq(savedOrder));
    }
}