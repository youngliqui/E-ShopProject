package by.youngliqui.EShopProject.mappers;

import by.youngliqui.EShopProject.dto.OrderDTO;
import by.youngliqui.EShopProject.models.Order;
import by.youngliqui.EShopProject.models.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OrderMapper {
    OrderMapper MAPPER = Mappers.getMapper(OrderMapper.class);

    Order toOrder(OrderDTO orderDTO);

    @InheritInverseConfiguration
    @Mapping(target = "nameOfUser", source = "user", qualifiedByName = "mapUserToUsername")
    OrderDTO fromOrder(Order order);

    List<Order> toOrderList(List<OrderDTO> orderDTOS);

    @Mapping(target = "nameOfUser", source = "user", qualifiedByName = "mapUserToUsername")
    List<OrderDTO> toOrderDTOs(List<Order> orders);

    @Named("mapUserToUsername")
    default String mapUserToUsername(User user) {
        return user != null ? user.getName() : null;
    }
}
