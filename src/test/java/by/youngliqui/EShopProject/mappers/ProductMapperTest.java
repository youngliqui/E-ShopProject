package by.youngliqui.EShopProject.mappers;

import by.youngliqui.EShopProject.dto.ProductDTO;
import by.youngliqui.EShopProject.models.Category;
import by.youngliqui.EShopProject.models.Product;
import by.youngliqui.EShopProject.models.Size;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;


class ProductMapperTest {
    private final ProductMapper mapper = ProductMapper.MAPPER;

    @Test
    void checkConvertProductDTOToProduct() {
        ProductDTO dto = ProductDTO.builder()
                .id(10L)
                .title("test")
                .price(BigDecimal.valueOf(12.12))
                .build();

        Product expectedProduct = Product.builder()
                .id(dto.getId())
                .price(dto.getPrice())
                .title(dto.getTitle())
                .build();

        var actualResult = mapper.toProduct(dto);

        assertAll(() -> assertThat(actualResult).isInstanceOf(Product.class),
                () -> assertThat(actualResult.getId()).isEqualTo(expectedProduct.getId()),
                () -> assertThat(actualResult.getTitle()).isEqualTo(expectedProduct.getTitle()),
                () -> assertThat(actualResult.getPrice()).isEqualTo(expectedProduct.getPrice()),
                () -> assertThat(actualResult.getDescription()).isNull(),
                () -> assertThat(actualResult.getCategories()).isNull(),
                () -> assertThat(actualResult.getSize()).isNull()
        );
    }

    @Test
    void checkConvertProductToProductDTO() {
        Product product = Product.builder()
                .id(11L)
                .title("test")
                .price(BigDecimal.valueOf(133.23))
                .categories(Collections.singletonList(Category.builder().build()))
                .description("desc")
                .size(Size.M)
                .build();

        ProductDTO expectedDTO = ProductDTO.builder()
                .id(product.getId())
                .price(product.getPrice())
                .title(product.getTitle())
                .build();

        var actualResult = mapper.fromProduct(product);

        assertAll(() -> assertThat(actualResult).isInstanceOf(ProductDTO.class),
                () -> assertThat(actualResult).isEqualTo(expectedDTO)
        );
    }

    @Test
    void checkConvertListOfProductDTOsToListOfProducts() {
        List<ProductDTO> dtoList = new ArrayList<>(List.of(
                ProductDTO.builder()
                        .id(11L)
                        .title("product1")
                        .price(BigDecimal.valueOf(12.23))
                        .build(),
                ProductDTO.builder()
                        .id(12L)
                        .title("product2")
                        .price(BigDecimal.valueOf(34.34))
                        .build()
        ));

        List<Product> expectedProduct = new ArrayList<>(List.of(
                Product.builder()
                        .id(dtoList.get(0).getId())
                        .title(dtoList.get(0).getTitle())
                        .price(dtoList.get(0).getPrice())
                        .size(null)
                        .description(null)
                        .categories(null)
                        .build(),
                Product.builder()
                        .id(dtoList.get(1).getId())
                        .title(dtoList.get(1).getTitle())
                        .price(dtoList.get(1).getPrice())
                        .size(null)
                        .description(null)
                        .categories(null)
                        .build()
        ));

        var actualResult = mapper.toProductList(dtoList);

        assertThat(actualResult.get(0)).isInstanceOf(Product.class);
        assertThat(actualResult).containsAnyElementsOf(expectedProduct);
    }

    @Test
    void checkConvertListOfProductsToListOfProductDTOs() {
        List<Product> products = new ArrayList<>(List.of(
                Product.builder()
                        .id(12L)
                        .title("product1")
                        .price(BigDecimal.valueOf(1233.21))
                        .size(Size.XL)
                        .description("desc1")
                        .categories(Collections.singletonList(Category.builder().build()))
                        .build(),
                Product.builder()
                        .id(13L)
                        .title("product2")
                        .price(BigDecimal.valueOf(123.1))
                        .size(Size.XL)
                        .description("desc2")
                        .categories(Collections.singletonList(Category.builder().build()))
                        .build()
        ));

        List<ProductDTO> expectedDTOs = new ArrayList<>(List.of(
                ProductDTO.builder()
                        .id(products.get(0).getId())
                        .title(products.get(0).getTitle())
                        .price(products.get(0).getPrice())
                        .build(),
                ProductDTO.builder()
                        .id(products.get(1).getId())
                        .title(products.get(1).getTitle())
                        .price(products.get(1).getPrice())
                        .build()
        ));

        var actualResult = mapper.fromProductList(products);

        assertThat(actualResult.get(0)).isInstanceOf(ProductDTO.class);
        assertThat(actualResult).containsAnyElementsOf(expectedDTOs);
    }
}