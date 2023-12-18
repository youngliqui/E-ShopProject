package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.ProductDTO;
import by.youngliqui.EShopProject.dto.ProductsResponse;
import by.youngliqui.EShopProject.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ProductsResponse list() {
        return new ProductsResponse(productService.getAll());
    }

    @PostMapping("/new")
    public ResponseEntity<HttpStatus> add(@RequestBody @Valid ProductDTO productDTO,
                                          BindingResult bindingResult) {
        // implements adding new product function
        return null;
    }

}
