package by.youngliqui.EShopProject.servicesImpl;

import by.youngliqui.EShopProject.dto.OrderDTO;
import by.youngliqui.EShopProject.dto.PaymentDTO;
import by.youngliqui.EShopProject.exceptions.OrderNotFoundException;
import by.youngliqui.EShopProject.models.Enums.OrderStatus;
import by.youngliqui.EShopProject.models.Enums.PaymentType;
import by.youngliqui.EShopProject.models.Order;
import by.youngliqui.EShopProject.models.Payment;
import by.youngliqui.EShopProject.repositories.OrderRepository;
import by.youngliqui.EShopProject.repositories.PaymentRepository;
import by.youngliqui.EShopProject.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static by.youngliqui.EShopProject.mappers.OrderMapper.MAPPER;

@Service
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        orderRepository.save(order);
    }

    @Override
    public List<OrderDTO> getOrdersByUsername(String username) {
        return MAPPER.toOrderDTOs(orderRepository.findAllByUser_NameIgnoreCase(username));
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(Long userId) {
        return MAPPER.toOrderDTOs(orderRepository.findAllByUser_Id(userId));
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public void updateOrderStatus(String status, Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new OrderNotFoundException("order with id " + orderId + " was not found")
        );

        switch (status) {
            case "new":
                order.setStatus(OrderStatus.NEW);
                break;

            case "paid":
                order.setStatus(OrderStatus.PAID);
                break;

            case "approved":
                order.setStatus(OrderStatus.APPROVED);
                break;

            case "closed":
                order.setStatus(OrderStatus.CLOSED);
                break;

            case "canceled":
                order.setStatus(OrderStatus.CANCELED);

            default:
                throw new IllegalArgumentException("status " + status + " not found");
        }

        orderRepository.save(order);
    }

    @Override
    @Transactional
    public void createOrderPayment(PaymentDTO paymentDTO, String username) {
        Order order = orderRepository.findById(paymentDTO.getOrderId()).orElseThrow(
                () -> new OrderNotFoundException("order with id " + paymentDTO.getOrderId() + " was not found")
        );

        if (!order.getUser().getName().equals(username)) {
            throw new OrderNotFoundException("order with id - " + order.getId() + " and username - " + username +
                    " was not found");
        }

        if (!order.getStatus().equals(OrderStatus.APPROVED)) {
            throw new IllegalArgumentException("order status is not approved");
        }

        Payment payment = Payment.builder()
                .order(order)
                .date(LocalDateTime.now())
                .amount(paymentDTO.getAmount())
                .build();

        if (!order.getSum().equals(payment.getAmount())) {
            throw new IllegalArgumentException("order sum and payment sum it not equals");
        }

        switch (paymentDTO.getPaymentType()) {
            case "cash" -> payment.setPaymentType(PaymentType.CASH);
            case "card" -> payment.setPaymentType(PaymentType.BY_CARD);
            case "crypto" -> payment.setPaymentType(PaymentType.CRYPTO);
            default ->
                    throw new IllegalArgumentException("this payment type - " + paymentDTO.getPaymentType() + " was not found");
        }

        order.setStatus(OrderStatus.PAID);

        orderRepository.save(order);
        paymentRepository.save(payment);
    }
}
