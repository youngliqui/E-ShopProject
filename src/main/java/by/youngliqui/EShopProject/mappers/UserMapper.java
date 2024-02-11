package by.youngliqui.EShopProject.mappers;

import by.youngliqui.EShopProject.dto.UserDTO;
import by.youngliqui.EShopProject.models.User;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper MAPPER = Mappers.getMapper(UserMapper.class);

    @Mapping(source = "username", target = "name")
    User toUser(UserDTO userDTO);

    @InheritInverseConfiguration
    @Mapping(source = "name", target = "username")
    @Mapping(source = "password", target = "matchingPassword")
    UserDTO fromUser(User user);

    @Mapping(source = "username", target = "name")
    List<User> toUsersList(List<UserDTO> userDTOS);

    @Mapping(source = "name", target = "username")
    @Mapping(source = "password", target = "matchingPassword")
    List<UserDTO> fromUsersList(List<User> usersList);
}
