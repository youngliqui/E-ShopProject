package by.youngliqui.EShopProject.mappers;

import by.youngliqui.EShopProject.dto.OrderDTO;
import by.youngliqui.EShopProject.dto.OrderDetailsDTO;
import by.youngliqui.EShopProject.models.Order;
import by.youngliqui.EShopProject.models.OrderDetails;
import by.youngliqui.EShopProject.models.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface OrderMapper {
    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    Order toOrder(OrderDTO orderDTO);

    @InheritInverseConfiguration
    @Mapping(target = "nameOfUser", source = "user", qualifiedByName = "mapUserToUsername")
    @Mapping(target = "details", source = "details", qualifiedByName = "mapOrderDetailsToDTO")
    OrderDTO fromOrder(Order order);

    List<Order> toOrderList(List<OrderDTO> orderDTOS);

    List<OrderDTO> toOrderDTOs(List<Order> orders);

    @Named("mapUserToUsername")
    default String mapUserToUsername(User user) {
        return user != null ? user.getName() : null;
    }

    @Named("mapOrderDetailsToDTO")
    default List<OrderDetailsDTO> mapOrderDetailsToDTO(List<OrderDetails> orderDetails) {
        return orderDetails.stream().map((detail) -> OrderDetailsDTO.builder()
                .productName(detail.getProduct().getTitle())
                .amount(detail.getAmount())
                .price(detail.getPrice())
                .build()).collect(Collectors.toList());
    }
}
