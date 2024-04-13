package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.ProductDTO;
import by.youngliqui.EShopProject.exceptions.ProductNotFoundException;
import by.youngliqui.EShopProject.models.*;
import by.youngliqui.EShopProject.models.Enums.Size;
import by.youngliqui.EShopProject.repositories.ProductRepository;
import by.youngliqui.EShopProject.servicesImpl.ProductServiceImpl;
import by.youngliqui.EShopProject.servicesImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    private ProductRepository productRepository;
    @Mock
    private UserServiceImpl userService;
    @Mock
    private BucketService bucketService;
    @InjectMocks
    private ProductServiceImpl productService;

    private final Product product1 = Product.builder()
            .id(1L)
            .title("product1")
            .price(BigDecimal.valueOf(23.23))
            .categories(Collections.singletonList(Category.builder().build()))
            .description("desc")
            .size(Size.L)
            .build();
    private final Product product2 = Product.builder()
            .id(2L)
            .title("product2")
            .price(BigDecimal.valueOf(232.22))
            .categories(Collections.singletonList(Category.builder().build()))
            .description("desc")
            .size(Size.M)
            .build();

    @Test
    void getAllProducts() {
        List<Product> products = new ArrayList<>(List.of(product1, product2));

        List<ProductDTO> expectedDTOs = new ArrayList<>(List.of(
                ProductDTO.builder()
                        .id(product1.getId())
                        .title(product1.getTitle())
                        .price(product1.getPrice())
                        .build(),
                ProductDTO.builder()
                        .id(product2.getId())
                        .title(product2.getTitle())
                        .price(product2.getPrice())
                        .build()
        ));

        doReturn(products).when(productRepository).findAll();

        var actualResult = productService.getAll();

        assertThat(actualResult.get(0)).isInstanceOf(ProductDTO.class);
        assertThat(actualResult).hasSize(2);
        assertThat(actualResult).containsAnyElementsOf(expectedDTOs);
    }

    @Test
    void getProductDTOById() {
        Product product = product1;
        ProductDTO expectedDTO = ProductDTO.builder()
                .id(product.getId())
                .title(product.getTitle())
                .price(product.getPrice())
                .build();

        doReturn(Optional.of(product)).when(productRepository).findById(product.getId());

        var actualResult = productService.getById(product.getId());

        verify(productRepository).findById(product.getId());
        assertThat(actualResult).isInstanceOf(ProductDTO.class);
        assertThat(actualResult).isEqualTo(expectedDTO);
    }

    @Test
    void throwExceptionIfProductIsNotFound() {
        doReturn(Optional.empty()).when(productRepository).findById(anyLong());

        long productId = 1L;
        assertAll(() -> {
            var exception = assertThrows(ProductNotFoundException.class,
                    () -> productService.getById(productId));
            assertThat(exception.getMessage()).isEqualTo("product with id = " + productId + " was not found");
        });
        verify(productRepository).findById(productId);
    }

    @Test
    void checkAddToUserBucketIfBucketIsNull() {
        User user = User.builder()
                .id(1L)
                .name("username")
                .bucket(null)
                .build();

        doReturn(user).when(userService).findByName(user.getName());

        productService.addToUserBucket(product1.getId(), user.getName());

        verify(userService).findByName(user.getName());
        verify(bucketService).createBucket(user, Collections.singletonList(product1.getId()));
        verify(userService).save(Mockito.eq(user));
    }

    @Test
    void throwExceptionIfUsernameIsNotFound() {
        String username = "username";
        doReturn(null).when(userService).findByName(username);

        assertAll(() -> {
                    var exception = assertThrows(UsernameNotFoundException.class,
                            () -> productService.addToUserBucket(1L, username));
                    assertThat(exception.getMessage()).isEqualTo("user not found with name: " + username);
                }
        );
    }

    @Test
    void checkAddToUserBucketIfBucketNotNull() {
        User user = User.builder()
                .id(1L)
                .name("username")
                .build();
        Bucket bucket = Bucket.builder()
                .id(10L)
                .build();
        user.setBucket(bucket);

        doReturn(user).when(userService).findByName(user.getName());

        productService.addToUserBucket(product1.getId(), user.getName());

        verify(userService).findByName(user.getName());
        verify(bucketService).addProducts(bucket, Collections.singletonList(product1.getId()));
    }

    @Test
    void shouldDeleteProductByCorrectId() {
        Long productId = 1L;
        Product product = Product.builder().id(productId).build();

        doReturn(Optional.of(product)).when(productRepository).findById(productId);

        productService.deleteProductById(productId);

        verify(productRepository).findById(productId);
        verify(productRepository).delete(product);
    }

    @Test
    void throwExceptionIfUserByIdNotFoundWhenDeleting() {
        long productId = -1L;

        assertAll(() -> {
            var exception = assertThrows(ProductNotFoundException.class,
                    () -> productService.deleteProductById(productId));
            assertThat(exception.getMessage()).isEqualTo("product with id " + productId + " was not found");
        });
    }

}