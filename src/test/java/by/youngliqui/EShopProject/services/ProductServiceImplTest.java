package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.ProductDTO;
import by.youngliqui.EShopProject.models.Category;
import by.youngliqui.EShopProject.models.Product;
import by.youngliqui.EShopProject.models.Size;
import by.youngliqui.EShopProject.repositories.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    void getEmptyProductDTOByIdIfProductIsNotFound() {
        ProductDTO expectedDTO = ProductDTO.builder().build();

        doReturn(Optional.empty()).when(productRepository).findById(anyLong());

        var actualResult = productService.getById(anyLong());

        verify(productRepository).findById(anyLong());
        assertThat(actualResult).isNotNull();
        assertThat(actualResult).isEqualTo(expectedDTO);
    }

    @Test
    void checkAddToUserBucketIfBucketIsNull() {

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

    }
}