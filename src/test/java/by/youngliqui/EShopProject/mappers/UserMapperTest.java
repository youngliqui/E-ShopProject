package by.youngliqui.EShopProject.mappers;

import by.youngliqui.EShopProject.dto.UserDTO;
import by.youngliqui.EShopProject.models.Bucket;
import by.youngliqui.EShopProject.models.Order;
import by.youngliqui.EShopProject.models.Role;
import by.youngliqui.EShopProject.models.User;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {
    private final UserMapper mapper = UserMapper.MAPPER;
    private final UserDTO userDTO = UserDTO.builder()
            .username("userDTO")
            .password("passDTO")
            .email("userdto@mail.com")
            .matchingPassword("passDTO")
            .build();

    private final User user = User.builder()
            .name("user")
            .id(9L)
            .role(Role.CLIENT)
            .email("user@gmail.com")
            .password("pass")
            .bucket(Bucket.builder().build())
            .orders(Collections.singletonList(Order.builder().build()))
            .archive(false)
            .build();

    @Test
    void checkConvertUserDTOToUser() {
        User expectedUser = User.builder()
                .name(userDTO.getUsername())
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();

        var actualResult = mapper.toUser(userDTO);

        assertThat(actualResult).isInstanceOf(User.class);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(expectedUser);
    }

    @Test
    void checkConvertUserToUserDTO() {
        UserDTO expectedDTO = UserDTO.builder()
                .username(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .build();

        var actualResult = mapper.fromUser(user);

        assertThat(actualResult).isInstanceOf(UserDTO.class);
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(expectedDTO);
    }

    @Test
    void checkConvertListOfUserDTOsToListOfUsers() {
        List<User> expectedUser = new ArrayList<>(Collections.singletonList(User.builder()
                .name(userDTO.getUsername())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .build()));

        var actualResult = mapper.toUsersList(Collections.singletonList(userDTO));

        assertThat(actualResult.get(0)).isInstanceOf(User.class);
        assertThat(actualResult).hasSize(1);
        assertThat(actualResult).containsAnyElementsOf(expectedUser);
    }
}