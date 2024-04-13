package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.UserDTO;
import by.youngliqui.EShopProject.exceptions.UserNotFoundException;
import by.youngliqui.EShopProject.models.Enums.Role;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.repositories.UserRepository;
import by.youngliqui.EShopProject.servicesImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

        doReturn(expectedUser).when(userRepository).findFirstByName(eq(name));

        User actualUser = userService.findByName(name);
        User randomUser = userService.findByName(UUID.randomUUID().toString());

        assertThat(actualUser).isNotNull();
        assertThat(actualUser).isEqualTo(expectedUser);

        assertThat(randomUser).isNull();
    }

    @Test
    void checkSaveIncorrectPassword() {
        UserDTO userDTO = UserDTO.builder()
                .password("pass")
                .matchingPassword("dummy")
                .build();

        assertThrows(RuntimeException.class, () -> userService.save(userDTO));
    }

    @Test
    void checkSave() {
        UserDTO userDTO = UserDTO.builder()
                .username("name")
                .password("pass")
                .matchingPassword("pass")
                .email("email@gmail.com")
                .build();

        doReturn("pass").when(passwordEncoder).encode(anyString());

        boolean result = userService.save(userDTO);

        assertThat(result).isTrue();
        verify(passwordEncoder).encode(anyString());
        verify(userRepository).save(any());
    }

    @Test
    void shouldReturnAllUsers() {
        User user1 = User.builder()
                .name("test user1")
                .id(10L)
                .email("test1@mail.com")
                .role(Role.CLIENT)
                .password("pass")
                .build();

        User user2 = User.builder()
                .name("test user2")
                .id(19L)
                .email("test2@mail.com")
                .role(Role.CLIENT)
                .password("pass")
                .build();

        doReturn(Arrays.asList(user1, user2)).when(userRepository).findAll();

        var actualResult = userService.getAll();

        assertThat(actualResult).hasSize(2);
        assertThat(actualResult.stream()).contains(user1, user2);
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenThereAreNoUsers() {
        doReturn(Collections.emptyList()).when(userRepository).findAll();

        var actualResult = userService.getAll();

        assertThat(actualResult).hasSize(0);
        verify(userRepository).findAll();
    }

    @ParameterizedTest
    @MethodSource("getUsers")
    void loadUsersByCorrectUsername(User user) {
        doReturn(user).when(userRepository).findFirstByName(user.getName());

        var actualResult = userService.loadUserByUsername(user.getName());

        assertThat(actualResult).isNotNull();
        verify(userRepository).findFirstByName(anyString());
        assertThat(actualResult.getUsername()).isEqualTo(user.getName());
        assertThat(actualResult.getPassword()).isEqualTo(user.getPassword());
        assertThat(actualResult.getAuthorities())
                .extracting(GrantedAuthority::getAuthority).contains(user.getRole().name());
    }

    @Test
    void throwExceptionIfUsernameIsNotFound() {
        doReturn(null).when(userRepository).findFirstByName(anyString());

        assertAll(() -> {
                    String username = "dummy";
                    var exception = assertThrows(
                            UsernameNotFoundException.class, () -> userService.loadUserByUsername(username)
                    );
                    assertThat(exception.getMessage()).isEqualTo("User not found with name " + username);
                }
        );
    }

    @Test
    void changeUserProfileIfUserDTOIsCorrect() {
        UserDTO userDTO = UserDTO.builder()
                .username("changed")
                .password("changed")
                .matchingPassword("changed")
                .email("changed@gmail.com")
                .build();

        User user = User.builder()
                .name("testUser")
                .id(10L)
                .email("test@mail.com")
                .role(Role.CLIENT)
                .password("pass")
                .build();

        doReturn(user).when(userRepository).findFirstByName(anyString());
        doReturn(userDTO.getPassword()).when(passwordEncoder).encode(anyString());

        userService.updateProfile(userDTO, user.getName());

        assertAll(() -> {
            assertThat(user.getName()).isEqualTo(userDTO.getUsername());
            assertThat(user.getPassword()).isEqualTo(userDTO.getPassword());
            assertThat(user.getEmail()).isEqualTo(userDTO.getEmail());
            assertThat(user.getRole()).isEqualTo(Role.CLIENT);
            assertThat(user.getId()).isEqualTo(10L);
        });
        verify(userRepository).findFirstByName(anyString());
        verify(userRepository).save(Mockito.eq(user));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void throwExceptionIfUserByNameIsNotFound() {
        doReturn(null).when(userRepository).findFirstByName(anyString());

        assertAll(() -> {
            String username = "dummy";
            var exception = assertThrows(UsernameNotFoundException.class, () ->
                    userService.updateProfile(UserDTO.builder().build(), username));
            assertThat(exception.getMessage()).isEqualTo("User not found with name " + username);
        });
    }

    @Test
    void doNotChangeUserProfileIfPassAndNameNullAndEmailsEqual() {
        UserDTO userDTO = UserDTO.builder()
                .username("name")
                .email("email")
                .build();

        User user = User.builder()
                .name("name")
                .email("email")
                .build();

        doReturn(user).when(userRepository).findFirstByName(anyString());

        userService.updateProfile(userDTO, user.getName());

        assertAll(() -> {
            assertThat(user.getEmail()).isEqualTo(userDTO.getEmail());
            assertThat(user.getPassword()).isNull();
            assertThat(user.getName()).isEqualTo(user.getName());
        });
        verify(userRepository).findFirstByName(anyString());
        verify(userRepository, Mockito.times(0)).save(any());
        verify(passwordEncoder, Mockito.times(0)).encode(anyString());
    }

    @Test
    void shouldDeleteUserById() {
        long userId = 0L;
        User user = User.builder().id(userId).build();

        doReturn(Optional.of(user)).when(userRepository).findById(userId);

        userService.deleteById(userId);

        verify(userRepository).delete(user);
    }

    @Test
    void shouldDeleteUserByUsername() {
        String username = "name";
        User user = User.builder().name(username).build();

        doReturn(user).when(userRepository).findFirstByName(username);

        userService.deleteByUsername(username);

        verify(userRepository).delete(user);
    }

    @Test
    void throwExceptionWhenUserByIdWasNotFound() {
        long userId = -1;
        assertAll(() -> {
            var exception = assertThrows(UserNotFoundException.class, () -> userService.deleteById(userId));
            assertThat(exception.getMessage()).isEqualTo("user with id " + userId + " was not found");
        });
    }

    @Test
    void throwExceptionWhenUserByUsernameWasNotFound() {
        doReturn(null).when(userRepository).findFirstByName(anyString());

        String username = "name";
        assertAll(() -> {
            var exception = assertThrows(UserNotFoundException.class, () -> userService.deleteByUsername(username));
            assertThat(exception.getMessage()).isEqualTo("user with username " + username + " was not found");
        });
    }

    private static Stream<Arguments> getUsers() {
        return Stream.of(
                Arguments.of(new User(1L, "user1", "pass1", "user1@gmail.com",
                        false, Role.CLIENT, null, null)),
                Arguments.of(new User(2L, "user2", "pass2", "user2@gmail.com",
                        false, Role.MANAGER, null, null)),
                Arguments.of(new User(3L, "user3", "pass3", "user3@gmail.com",
                        true, Role.ADMIN, null, null))
        );
    }
}