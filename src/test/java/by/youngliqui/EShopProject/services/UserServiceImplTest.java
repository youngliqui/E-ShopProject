package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;

    @Test
    void checkFindByName() {
        String name = "petr";
        User expectedUser = User.builder().build();

        doReturn(expectedUser).when(userRepository).findFirstByName(anyString());

        User actualUser = userService.findByName(name);

        assertThat(actualUser).isNotNull();
        assertThat(expectedUser).isEqualTo(actualUser);
    }

    @Test
    void checkFindByNameExact() {
        String name = "petr";
        User expectedUser = User.builder().build();

        doReturn(expectedUser).when(userRepository).findFirstByName(Mockito.eq(name));

        User actualUser = userService.findByName(name);
        User randomUser = userService.findByName(UUID.randomUUID().toString());

        assertThat(actualUser).isNotNull();
        assertThat(actualUser).isEqualTo(expectedUser);

        assertThat(randomUser).isNull();
    }
}