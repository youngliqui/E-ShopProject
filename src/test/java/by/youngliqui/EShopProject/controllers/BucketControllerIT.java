package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.models.Bucket;
import by.youngliqui.EShopProject.models.Product;
import by.youngliqui.EShopProject.models.Role;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.repositories.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BucketControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserRepository userRepository;

    private List<Product> products = new ArrayList<>(List.of(Product.builder()
                    .id(1L)
                    .title("product1")
                    .price(BigDecimal.valueOf(10.10))
                    .build(),
            Product.builder()
                    .id(2L)
                    .title("product2")
                    .price(BigDecimal.valueOf(12.12))
                    .build()));
    private User user = User.builder()
            .id(1L)
            .name("test")
            .password("test")
            .email("email@gmail.com")
            .role(Role.CLIENT)
            .archive(false)
            .build();

    @BeforeEach
    void setUp() {
        user.setBucket(new Bucket(5L, user, products));
        userRepository.save(user);
    }

    @Test
    void testCommitBucket() {
        ResponseEntity<Void> response = testRestTemplate.withBasicAuth("test", "test")
                .postForEntity("/bucket", null, Void.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

}
