package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.UserDTO;
import by.youngliqui.EShopProject.dto.UsersResponse;
import by.youngliqui.EShopProject.mappers.UserMapper;
import by.youngliqui.EShopProject.models.Role;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.repositories.UserRepository;
import by.youngliqui.EShopProject.services.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("user")
class UserControllerTestIT {
    private final TestRestTemplate testRestTemplate;
    private final UserService userService;
    private final UserRepository userRepository;
    private final UserMapper mapper = UserMapper.MAPPER;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final User IVAN = User.builder()
            .name("Ivan")
            .password(encoder.encode("ivan"))
            .role(Role.CLIENT)
            .email("ivan@gmail.com")
            .build();

    private final User MAX = User.builder()
            .name("Max")
            .password(encoder.encode("max"))
            .role(Role.CLIENT)
            .email("max@gmail.com")
            .build();

    private final User ADMIN = User.builder()
            .name("admin")
            .password(encoder.encode("admin"))
            .role(Role.ADMIN)
            .build();

    private final User MANAGER = User.builder()
            .name("manager")
            .password(encoder.encode("manager"))
            .role(Role.MANAGER)
            .build();

    @Autowired
    UserControllerTestIT(TestRestTemplate testRestTemplate, UserService userService, UserRepository userRepository) {
        this.testRestTemplate = testRestTemplate;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @DisplayName("test get all users functionality")
    @Nested
    class GetAllUsersTest {
        @BeforeEach
        void prepare() {
            System.out.println("get all users before each");
            userRepository.saveAll(List.of(IVAN, MAX, ADMIN, MANAGER));
        }

        @Test
        void checkGetAllUsersWithAdminAuthority() {
            ResponseEntity<UsersResponse> actualResult = testRestTemplate.withBasicAuth("admin", "admin")
                    .getForEntity("/users", UsersResponse.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(Objects.requireNonNull(actualResult.getBody()).getUsers())
                    .containsAnyOf(mapper.fromUser(IVAN), mapper.fromUser(MAX),
                            mapper.fromUser(ADMIN), mapper.fromUser(MANAGER));
        }

        @Test
        void checkGetAllUsersWithManagerAuthority() {
            ResponseEntity<UsersResponse> actualResult = testRestTemplate.withBasicAuth("manager", "manager")
                    .getForEntity("/users", UsersResponse.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(Objects.requireNonNull(actualResult.getBody()).getUsers())
                    .containsAnyOf(mapper.fromUser(IVAN), mapper.fromUser(MAX),
                            mapper.fromUser(ADMIN), mapper.fromUser(MANAGER));
        }

        @Test
        void checkGetAllUsersWithClientAuthority() {
            ResponseEntity<UsersResponse> actualResult = testRestTemplate.withBasicAuth("Ivan", "ivan")
                    .getForEntity("/users", UsersResponse.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(actualResult.getBody().getUsers()).isNull();
        }

        @Test
        void checkGetAllUsersWithEmptyAuthority() {
            ResponseEntity<UsersResponse> actualResult = testRestTemplate.withBasicAuth("dummy", "dummy")
                    .getForEntity("/users", UsersResponse.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(actualResult.getBody().getUsers()).isNull();
        }

        @AfterEach
        void deleteUsers() {
            System.out.println("get roles users after each");
            userRepository.deleteAllInBatch(List.of(IVAN, MAX, ADMIN, MANAGER));
        }
    }

    @Nested
    @Tag("save")
    @DisplayName("test save users functionality")
    class SaveUsersTest {
        UserDTO savedUser = UserDTO.builder()
                .username("user")
                .password("pass")
                .matchingPassword("pass")
                .email("user@gmail.com")
                .build();

        @BeforeEach
        void prepare() {
            System.out.println("save users before each");
            userRepository.saveAll(List.of(IVAN, MAX, ADMIN, MANAGER));
        }

        @Test
        void checkSaveUserWithAdminAuthentication() {
            User expectedUser = mapper.toUser(savedUser);


            ResponseEntity<Void> actualResult = testRestTemplate.withBasicAuth("admin", "admin")
                    .postForEntity("/users/new", savedUser, Void.class);

            var userResult = userRepository.findFirstByName(savedUser.getUsername());


            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(userResult).isNotNull();
            assertAll(() -> assertThat(userResult.getName()).isEqualTo(expectedUser.getName()),
                    () -> assertThat(userResult.getEmail()).isEqualTo(expectedUser.getEmail()),
                    () -> assertThat(userResult.getRole()).isEqualTo(Role.CLIENT),
                    () -> assertThat(userResult.isArchive()).isEqualTo(expectedUser.isArchive())
            );
            assertThat(encoder.matches(expectedUser.getPassword(), userResult.getPassword())).isTrue();
        }

        @Test
        void checkSaveUserWithManagerAuthentication() {
            ResponseEntity<Void> actualResult = testRestTemplate.withBasicAuth("manager", "manager")
                    .postForEntity("/users/new", savedUser, Void.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
            var userResult = userRepository.findFirstByName(savedUser.getUsername());
            assertThat(userResult).isNotNull();
        }

        @Test
        void checkSaveUserWithClientAuthentication() {
            ResponseEntity<Void> actualResult = testRestTemplate.withBasicAuth("Max", "max")
                    .postForEntity("/users/new", savedUser, Void.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        }

        @Test
        void checkSaveUserWithEmptyAuthentication() {
            ResponseEntity<Void> actualResult = testRestTemplate.withBasicAuth("dummy", "dummy")
                    .postForEntity("/users/new", savedUser, Void.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @AfterEach
        void deleteUsers() {
            System.out.println("get roles users after each");
            userRepository.deleteAllInBatch(List.of(IVAN, MAX, ADMIN, MANAGER));
        }
    }

    @Nested
    @Tag("role")
    @DisplayName("test get user roles functionality")
    class GetRolesTest {
        @BeforeEach
        void prepare() {
            System.out.println("get roles users before each");
            userRepository.saveAll(List.of(IVAN, MAX, ADMIN, MANAGER));
        }

        @Test
        void checkGetRolesWithAdminAuthentication() {
            ResponseEntity<String> actualResult = testRestTemplate.withBasicAuth(ADMIN.getName(), "admin")
                    .getForEntity("/users/" + ADMIN.getName() + "/roles", String.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(actualResult.getBody()).isEqualTo(ADMIN.getRole().name());
        }

        @Test
        void checkGetRolesWithManagerAuthentication() {
            ResponseEntity<String> actualResult = testRestTemplate.withBasicAuth(MANAGER.getName(), "manager")
                    .getForEntity("/users/" + MANAGER.getName() + "/roles", String.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(actualResult.getBody()).isEqualTo(MANAGER.getRole().name());
        }

        @Test
        void checkGetRolesWithClientAuthentication() {
            ResponseEntity<String> actualResult = testRestTemplate.withBasicAuth(IVAN.getName(), "ivan")
                    .getForEntity("/users/" + IVAN.getName() + "/roles", String.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(actualResult.getBody()).isEqualTo(IVAN.getRole().name());
        }

        @Test
        void checkGetRolesWithEmptyAuthentication() {
            ResponseEntity<String> actualResult = testRestTemplate.withBasicAuth("dummy", "dummy")
                    .getForEntity("/users/dummy/roles", String.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @AfterEach
        void deleteUsers() {
            System.out.println("get roles users after each");
            userRepository.deleteAllInBatch(List.of(IVAN, MAX, ADMIN, MANAGER));
        }
    }

    @Nested
    @Tag("profile")
    @DisplayName("test user profile functionality")
    class ProfileUserTest {
        @BeforeEach
        void prepare() {
            System.out.println("get roles users before each");
            userRepository.saveAll(List.of(IVAN, MAX, ADMIN, MANAGER));
        }

        @Test
        void throwExceptionWhenProfileUserWithEmptyAuthentication() {
            ResponseEntity<UserDTO> actualResult = testRestTemplate.getForEntity("/users/profile", UserDTO.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        void shouldGetProfileIfUserHasAuthority() {
            UserDTO expectedDTO = mapper.fromUser(IVAN);
            expectedDTO.setPassword(null);
            expectedDTO.setMatchingPassword(null);

            ResponseEntity<UserDTO> actualResult = testRestTemplate
                    .withBasicAuth(IVAN.getName(), "ivan")
                    .getForEntity("/users/profile", UserDTO.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(actualResult.getBody()).isEqualTo(expectedDTO);
        }

        @Test
        void checkUpdateProfile() {
            UserDTO updatedDTO = UserDTO.builder()
                    .username("updated")
                    .email("upd@gmail.com")
                    .password("updated")
                    .matchingPassword("updated")
                    .build();

            ResponseEntity<Void> actualResult = testRestTemplate.withBasicAuth("Ivan", "ivan")
                    .postForEntity("/users/profile", updatedDTO, Void.class);

            var updatedUser = userRepository.findFirstByName(updatedDTO.getUsername());

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertAll(() -> assertThat(updatedUser.getName()).isEqualTo(updatedDTO.getUsername()),
                    () -> assertThat(updatedUser.getEmail()).isEqualTo(updatedDTO.getEmail()),
                    () -> assertThat(encoder.matches(updatedDTO.getPassword(), updatedUser.getPassword())).isTrue(),
                    () -> assertThat(updatedUser.getRole()).isEqualTo(IVAN.getRole())
            );
        }

        @Test
        void checkUpdateProfileIfUserHasNotAuthorize() {
            ResponseEntity<Void> actualResult =
                    testRestTemplate.postForEntity("/users/profile", UserDTO.builder().build(), Void.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        }

        @Test
        void checkUpdateProfileIfUserHasNotMatchingPassword() {
            ResponseEntity<Void> actualResult =
                    testRestTemplate.withBasicAuth("Ivan", "ivan")
                            .postForEntity("/users/profile", UserDTO.builder()
                                    .password("pass")
                                    .password("dummy")
                                    .build(), Void.class
                            );
        }

        @AfterEach
        void deleteUsers() {
            System.out.println("get roles users after each");
            userRepository.deleteAllInBatch(List.of(IVAN, MAX, ADMIN, MANAGER));
        }

    }
}