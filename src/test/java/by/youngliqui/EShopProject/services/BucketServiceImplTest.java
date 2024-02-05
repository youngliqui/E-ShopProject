package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.BucketDTO;
import by.youngliqui.EShopProject.dto.BucketDetailsDTO;
import by.youngliqui.EShopProject.models.Bucket;
import by.youngliqui.EShopProject.models.Product;
import by.youngliqui.EShopProject.models.Role;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.repositories.BucketRepository;
import by.youngliqui.EShopProject.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BucketServiceImplTest {
    @Mock
    private BucketRepository bucketRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private OrderService orderService;
    @Mock
    private UserService userService;
    @InjectMocks
    private BucketServiceImpl bucketService;

    private final Product product1 = Product.builder()
            .id(1L)
            .price(BigDecimal.valueOf(12.12))
            .title("product1")
            .build();

    private final Product product2 = Product.builder()
            .id(2L)
            .price(BigDecimal.valueOf(23.32))
            .title("product2")
            .build();

    @Test
    void checkCreateBucket() {
        User user = User.builder()
                .name("user")
                .id(52L)
                .password("pass")
                .role(Role.CLIENT)
                .build();

        Bucket expectedBucket = Bucket.builder()
                .user(user)
                .products(Arrays.asList(product1, product2))
                .build();

        doReturn(Optional.of(product1)).when(productRepository).findById(product1.getId());
        doReturn(Optional.of(product2)).when(productRepository).findById(product2.getId());

        Bucket resultBucket = bucketService.createBucket(user, Arrays.asList(product1.getId(), product2.getId()));

        assertAll(() -> {
            assertThat(resultBucket).isNotNull();
            assertThat(resultBucket.getUser()).isEqualTo(user);
            assertThat(resultBucket.getProducts()).hasSize(2);
            assertThat(resultBucket.getProducts()).containsOnly(product1, product2);
        });
        Mockito.verify(bucketRepository).save(Mockito.eq(expectedBucket));
    }

    @Test
    void checkAddingProductsToBucket() {
        Bucket bucket = Bucket.builder().build();

        doReturn(Optional.of(product1)).when(productRepository).findById(product1.getId());
        doReturn(Optional.of(product2)).when(productRepository).findById(product2.getId());

        bucketService.addProducts(bucket, Arrays.asList(product1.getId(), product2.getId()));

        assertThat(bucket.getProducts()).contains(product1, product2);
        Mockito.verify(bucketRepository).save(bucket);
    }

    @Test
    void checkAddingProductsToBucketIfProductsIsEmpty() {
        Bucket bucket = Bucket.builder().build();

        Mockito.lenient().doReturn(Optional.empty()).when(productRepository).findById(anyLong());

        bucketService.addProducts(bucket, Collections.emptyList());

        assertThat(bucket.getProducts()).isNotNull();
        assertThat(bucket.getProducts()).hasSize(0);
        Mockito.verify(bucketRepository).save(bucket);
    }

    @Test
    void shouldGetBucketByUsername() {
        User user = User.builder()
                .id(10L)
                .name("name")
                .email("email")
                .password("pass")
                .bucket(Bucket.builder()
                        .id(10L)
                        .products(Arrays.asList(product1, product2))
                        .build())
                .build();

        BucketDTO expectedBucket = new BucketDTO(2, 35.44,
                Arrays.asList(new BucketDetailsDTO(product1), new BucketDetailsDTO(product2)));

        doReturn(user).when(userService).findByName(user.getName());

        BucketDTO actualResult = bucketService.getBucketByUser(user.getName());

        assertThat(actualResult).isEqualTo(expectedBucket);
        verify(userService).findByName(user.getName());
    }

    @Test
    void shouldGetEmptyBucketIfUserIsNotFound() {
        doReturn(null).when(userService).findByName(anyString());

        BucketDTO bucketDTO = bucketService.getBucketByUser("not existing username");

        assertThat(bucketDTO).isNotNull();
        assertThat(bucketDTO).isEqualTo(new BucketDTO());
    }
}