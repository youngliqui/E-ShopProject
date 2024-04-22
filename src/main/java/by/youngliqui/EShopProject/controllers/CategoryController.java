package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.models.Category;
import by.youngliqui.EShopProject.services.CategoryService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@Tag(name = "Категории", description = "методы для работы с категориями")
@SecurityRequirement(name = "basicAuth")
public class CategoryController {
    private final CategoryService categoryService;


    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "Получение всех категорий")
    public List<Category> getAll() {
        return categoryService.getAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/new")
    @Operation(summary = "Добавление новой категории")
    public ResponseEntity<HttpStatus> addCategory(@RequestBody Category category) {
        categoryService.save(category);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получение категории по id")
    public Category getCategoryById(
            @Parameter(description = "id категории")
            @PathVariable("id") Long id) {

        return categoryService.getById(id);
    }

    @GetMapping("name/{name}")
    @Operation(summary = "Получение категории по названию")
    public Category getCategoryByName(
            @Parameter(description = "название категории")
            @PathVariable("name") String name) {

        return categoryService.getByName(name);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "удаление категории по id")
    public ResponseEntity<HttpStatus> deleteCategoryById(
            @Parameter(description = "id категории")
            @PathVariable("id") Long id) {

        categoryService.deleteCategoryById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping(value = "/add")
    @Operation(summary = "добавление категории к товару по id")
    public ResponseEntity<HttpStatus> addCategoryToProductById(
            @Parameter(description = "id категории") @RequestParam(value = "categoryId") Long categoryId,
            @Parameter(description = "id товара") @RequestParam(value = "productId") Long productId) {

        categoryService.addCategoryToProductById(productId, categoryId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping(value = "/add/name")
    @Operation(summary = "добавление категории к товару по названию")
    public ResponseEntity<HttpStatus> addCategoryToProductById(
            @Parameter(description = "название категории") @RequestParam(value = "categoryName") String categoryName,
            @Parameter(description = "id товара") @RequestParam(value = "productId") Long productId) {

        categoryService.addCategoryToProductByName(productId, categoryName);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
