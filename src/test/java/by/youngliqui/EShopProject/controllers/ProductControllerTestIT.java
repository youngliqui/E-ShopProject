package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.mappers.ProductMapper;
import by.youngliqui.EShopProject.models.Product;
import by.youngliqui.EShopProject.models.Enums.Role;
import by.youngliqui.EShopProject.models.Enums.Size;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.repositories.ProductRepository;
import by.youngliqui.EShopProject.repositories.UserRepository;
import by.youngliqui.EShopProject.services.ProductService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Tag("product")
public class ProductControllerTestIT {
    private final TestRestTemplate testRestTemplate;
    private final ProductService productService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper = ProductMapper.MAPPER;
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    private final Product product1 = Product.builder()
            .title("product1").price(BigDecimal.valueOf(100)).size(Size.XXL).id(1L)
            .build();

    private final Product product2 = Product.builder()
            .title("product2").price(BigDecimal.valueOf(200)).size(Size.M).id(2L)
            .build();

    private final User USER = User.builder()
            .name("user").password(encoder.encode("user")).role(Role.CLIENT).email("user@gmail.com")
            .build();

    private final User ADMIN = User.builder()
            .name("admin").password(encoder.encode("admin")).role(Role.ADMIN).build();

    private final User MANAGER = User.builder().name("manager").password(encoder.encode("manager"))
            .role(Role.MANAGER)
            .build();

    @Autowired
    public ProductControllerTestIT(TestRestTemplate testRestTemplate, ProductService productService,
                                   ProductRepository productRepository, UserRepository userRepository) {
        this.testRestTemplate = testRestTemplate;
        this.productService = productService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Nested
    @Tag("delete")
    @DisplayName("test delete products functionality")
    class DeleteProduct {

        @BeforeEach
        void prepare() {
            productRepository.saveAll(List.of(product1, product2));
            userRepository.saveAll(List.of(USER, ADMIN, MANAGER));
        }

        @Test
        void shouldDeleteProductByCorrectIdWithAdminAuth() {
            Long productId = 1L;
            assertThat(productRepository.findAll()).hasSize(2);

            ResponseEntity<Void> actualResult = testRestTemplate
                    .withBasicAuth("admin", "admin")
                    .exchange("/products/" + productId, HttpMethod.DELETE, null, Void.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(productRepository.findById(productId)).isEmpty();
            assertThat(productRepository.findAll()).hasSize(1);
        }

        @Test
        void shouldDeleteProductByCorrectIdWithManagerAuth() {
            long productId = 1L;
            assertThat(productRepository.findAll()).hasSize(2);

            ResponseEntity<Void> actualResult = testRestTemplate
                    .withBasicAuth("manager", "manager")
                    .exchange("/products/" + productId, HttpMethod.DELETE, null, Void.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(productRepository.findById(productId)).isEmpty();
            assertThat(productRepository.findAll()).hasSize(1);
        }

        @Test
        void checkDeleteProductByIdWhenAuthIsNull() {
            long productId = 1L;
            assertThat(productRepository.findAll()).hasSize(2);

            ResponseEntity<Void> actualResult = testRestTemplate
                    .withBasicAuth("dummy", "dummy")
                    .exchange("/products/" + productId, HttpMethod.DELETE, null, Void.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
            assertThat(productRepository.findAll()).hasSize(2);
        }

        @Test
        void checkDeleteProductByIdWhenAuthIsUser() {
            long productId = 1L;
            assertThat(productRepository.findAll()).hasSize(2);

            ResponseEntity<Void> actualResult = testRestTemplate
                    .withBasicAuth("user", "user")
                    .exchange("/products/" + productId, HttpMethod.DELETE, null, Void.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
            assertThat(productRepository.findAll()).hasSize(2);
        }

        @Test
        void checkDeleteProductByIncorrectId() {
            long productId = -1000;
            assertThat(productRepository.findAll()).hasSize(2);

            ResponseEntity<Void> actualResult = testRestTemplate
                    .withBasicAuth("admin", "admin")
                    .exchange("/product/" + productId, HttpMethod.DELETE, null, Void.class);

            assertThat(actualResult.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
            assertThat(productRepository.findAll()).hasSize(2);
        }

        @AfterEach
        void deleteAll() {
            productRepository.deleteAllInBatch();
            userRepository.deleteAllInBatch();
        }
    }
}
