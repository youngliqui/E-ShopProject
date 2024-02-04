package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.ProductDTO;
import by.youngliqui.EShopProject.services.ProductService;
import by.youngliqui.EShopProject.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private UserService userService;
    @Autowired
    private ObjectMapper objectMapper;
    @InjectMocks
    private ProductController productController;

    private final ProductDTO dto1 = ProductDTO.builder()
            .id(998L)
            .title("Test product 998")
            .price(BigDecimal.valueOf(998.98))
            .build();
    private final ProductDTO dto2 = ProductDTO.builder()
            .id(999L)
            .title("Test product 999")
            .price(BigDecimal.valueOf(999.99))
            .build();

    @BeforeEach
    void SetUp() {
        given(productService.getAll()).willReturn(Arrays.asList(dto1, dto2));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void checkList() throws Exception {
        String expectedJson = "{\"products\":["
                + objectMapper.writeValueAsString(dto1) + "," + objectMapper.writeValueAsString(dto2) + "]}";
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/products"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content()
                        .json(expectedJson)
                );
    }
}