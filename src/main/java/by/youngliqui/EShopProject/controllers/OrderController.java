package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.OrderDTO;
import by.youngliqui.EShopProject.dto.PaymentDTO;
import by.youngliqui.EShopProject.exceptions.UserNotAuthorizeException;
import by.youngliqui.EShopProject.services.OrderService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
@Tag(name = "Заказы", description = "методы для работы с заказами")
@OpenAPIDefinition(info = @Info(title = "E-SHOP API", version = "v1"))
@SecurityRequirement(name = "basicAuth")
public class OrderController {
    private final OrderService orderService;


    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(summary = "получение всех заказов")
    public List<OrderDTO> getAll() {
        return orderService.getAllOrders();
    }

    @GetMapping("username/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(summary = "получение заказов у пользователя по username")
    public List<OrderDTO> getUserOrdersByUsername(@Parameter(description = "имя пользователя")
                                                  @PathVariable("username") String username) {

        return orderService.getOrdersByUsername(username);
    }

    @GetMapping("userId/{userId}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(summary = "получение заказов у пользователя по id пользователя")
    public List<OrderDTO> getUserOrdersByUserId(@Parameter(description = "id пользователя")
                                                @PathVariable("userId") Long userId) {

        return orderService.getOrdersByUserId(userId);
    }

    @GetMapping("/user")
    @Operation(summary = "просмотр заказов пользователем")
    public List<OrderDTO> getUserOrders(Principal principal) {
        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        String username = principal.getName();
        return orderService.getOrdersByUsername(username);
    }

    @PatchMapping(value = "{orderId}/status")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(summary = "изменение статуса заказа")
    public ResponseEntity<Void> updateOrderStatus(@Parameter(description = "id заказа")
                                                  @PathVariable("orderId") Long orderId,
                                                  @Parameter(description = "статус заказа")
                                                  @RequestBody String status) {

        orderService.updateOrderStatus(status, orderId);

        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "{id}/payment")
    @Operation(summary = "опалата заказа")
    public ResponseEntity<Void> createOrderPayment(@Parameter(description = "id заказа")
                                                   @PathVariable("id") Long orderId,
                                                   @Parameter(description = "тело оплаты")
                                                   @RequestBody PaymentDTO paymentDTO,
                                                   Principal principal) {

        if (principal == null) {
            throw new UserNotAuthorizeException("you are not authorize");
        }

        orderService.createOrderPayment(orderId, paymentDTO, principal.getName());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
