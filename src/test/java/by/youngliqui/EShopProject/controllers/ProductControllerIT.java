package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.ProductDTO;
import by.youngliqui.EShopProject.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerIT {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @MockBean
    private ProductService productService;
    private final ProductDTO expectedProduct = ProductDTO.builder()
            .id(99L)
            .title("Test product")
            .price(BigDecimal.valueOf(999.99))
            .build();

    @BeforeEach
    void setUp() {
        given(productService.getById(expectedProduct.getId())).willReturn(expectedProduct);
    }

    @Test
    void getById() {
        ResponseEntity<ProductDTO> entity =
                testRestTemplate.getForEntity("/products/" + expectedProduct.getId(), ProductDTO.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ProductDTO actualProduct = entity.getBody();
        assertThat(actualProduct).isEqualTo(expectedProduct);
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    void addProduct() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<ProductDTO> request = new HttpEntity<>(expectedProduct, headers);

        ResponseEntity<Void> entity =
                testRestTemplate.postForEntity("/products/new", request, Void.class);

        assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        verify(productService).addProduct(Mockito.eq(expectedProduct));

        ArgumentCaptor<ProductDTO> captor = ArgumentCaptor.forClass(ProductDTO.class);
        verify(productService).addProduct(captor.capture());

        assertThat(captor.getValue()).isEqualTo(expectedProduct);
    }
}