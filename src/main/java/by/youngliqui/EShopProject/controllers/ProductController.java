package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.ProductDTO;
import by.youngliqui.EShopProject.dto.ProductsResponse;
import by.youngliqui.EShopProject.exceptions.ProductNotCreatedException;
import by.youngliqui.EShopProject.services.ProductService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Продукты", description = "методы для работы с продуктами")
@OpenAPIDefinition(info = @Info(title = "E-SHOP API", version = "v1"))
@SecurityRequirement(name = "basicAuth")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    @Operation(summary = "получение списка всех товаров")
    public ProductsResponse list() {
        return new ProductsResponse(productService.getAll());
    }


    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @Operation(summary = "добавление нового продукта")
    public ResponseEntity<HttpStatus> addProduct(@RequestBody @Valid ProductDTO productDTO,
                                                 BindingResult bindingResult) {
        StringBuilder errorMsg = new StringBuilder();

        if (bindingResult.hasErrors()) {
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMsg
                        .append(error.getField())
                        .append(" - ")
                        .append(error.getDefaultMessage())
                        .append(";");
            }

            throw new ProductNotCreatedException(errorMsg.toString());
        }

        productService.addProduct(productDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}/bucket")
    @Operation(summary = "добавление товара в корзину пользователя")
    public ResponseEntity<HttpStatus> addBucket(@Parameter(description = "уникальный индентификатор товара")
                                                @PathVariable Long id, Principal principal) {
        // if principal == null -> redirect to products
        if (principal == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        productService.addToUserBucket(id, principal.getName());

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "получение товара по id")
    public ProductDTO getById(@Parameter(description = "уникальный индентификатор товара") @PathVariable Long id) {
        return productService.getById(id);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "удаление товара по id")
    public ResponseEntity<Void> deleteProductById(@Parameter(description = "уникальный идентификатор товара")
                                                  @PathVariable("id") Long id) {

        productService.deleteProductById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("{id}/tags")
    @Operation(summary = "добавление тегов к товару")
    public ResponseEntity<Void> addTagsToProduct(@Parameter(description = "id продукта")
                                                 @PathVariable("id") Long productId,
                                                 @Parameter(description = "список тегов")
                                                 @RequestBody TagsRequest tagsRequest) {

        productService.addTagsToProduct(productId, tagsRequest.getTags());

        return ResponseEntity.ok().build();
    }

    private static class TagsRequest {
        private List<String> tags;

        public List<String> getTags() {
            return tags;
        }
    }

    @GetMapping(params = {"page", "size"})
    @Operation(summary = "получение страниц с товарами")
    public List<ProductDTO> getFilteredAndSortedProducts(
            @Parameter(description = "наименование") @RequestParam(value = "title", required = false) String name,
            @Parameter(description = "сортировка {priceAsc/priceDesc}")
            @RequestParam(value = "sortBy", defaultValue = "default", required = false) String sortBy,
            @Parameter(description = "номер страницы") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "количество товаров на странице")
            @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        return productService.getFilteredAndSortedProducts(name, sortBy, page, size);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping(value = "/{id}/quantity", params = {"action", "quantity"})
    @Operation(summary = "изменение количества товара")
    public ResponseEntity<Void> updateProductQuantity(@Parameter(description = "id продукта")
                                                      @PathVariable("id") Long id,
                                                      @Parameter(description = "действие (inc/dec)")
                                                      @RequestParam(value = "action") String action,
                                                      @Parameter(description = "количество")
                                                      @RequestParam(value = "quantity") Integer quantity) {

        if ("inc".equals(action)) {
            productService.addQuantity(quantity, id);
        } else if ("dec".equals(action)) {
            productService.subtractQuantity(quantity, id);
        } else {
            throw new IllegalArgumentException("Invalid action: " + action);
        }

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PutMapping(value = "/{id}/availability")
    @Operation(summary = "обновление доступности товара")
    public ResponseEntity<Void> updateProductAvailability(@Parameter(description = "id товара")
                                                          @PathVariable("id") Long id,
                                                          @RequestBody @Valid AvailabilityRequest availabilityRequest) {

        if (availabilityRequest.isAvailable()) {
            productService.setAvailable(id);
        } else {
            productService.setUnavailable(id);
        }

        return ResponseEntity.ok().build();
    }

    private static class AvailabilityRequest {
        private boolean available;

        public boolean isAvailable() {
            return available;
        }

        public void setAvailable(boolean available) {
            this.available = available;
        }
    }

}
