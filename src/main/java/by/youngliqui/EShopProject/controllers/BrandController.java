package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.BrandDTO;
import by.youngliqui.EShopProject.mappers.BrandMapper;
import by.youngliqui.EShopProject.services.BrandService;
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
@RequestMapping("/brands")
@Tag(name = "бренды", description = "методы работы с брендами")
@SecurityRequirement(name = "basicAuth")
public class BrandController {
    private final BrandService brandService;
    private final BrandMapper brandMapper = BrandMapper.MAPPER;

    @Autowired
    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping
    @Operation(summary = "Получение всех брендов")
    public List<BrandDTO> getBrands() {
        return brandService.getAll();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @PostMapping("/new")
    @Operation(summary = "Добавление нового бренда")
    public ResponseEntity<HttpStatus> createBrand(@RequestBody BrandDTO brandDTO) {
        brandService.save(brandDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Удаление бренда по id")
    public ResponseEntity<HttpStatus> deleteBrandById(@Parameter(description = "id бренда")
                                                      @PathVariable("id") Long id) {

        brandService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @DeleteMapping("name/{name}")
    @Operation(summary = "Удаление бренда по названию")
    public ResponseEntity<HttpStatus> deleteBrandByName(@Parameter(description = "название бренда")
                                                        @PathVariable("name") String name) {
        brandService.deleteByUsername(name);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
    }

    @GetMapping("/{id}")
    @ResponseBody
    @Operation(summary = "Получение бренда по id")
    public BrandDTO getBrandById(@Parameter(description = "id бренда")
                                 @PathVariable("id") Long id) {

        return brandService.findById(id);
    }

    @GetMapping("name/{name}")
    @ResponseBody
    @Operation(summary = "Получение бренда по названию")
    public BrandDTO getBrandByName(@Parameter(description = "название бренда")
                                   @PathVariable("name") String name) {

        return brandService.findByName(name);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'MANAGER')")
    @GetMapping(value = "/add", params = {"brandId", "productId"})
    @Operation(summary = "Добавление бренда к товарам по id")
    public ResponseEntity<HttpStatus> addBrandToProductsById(@Parameter(description = "id бренда")
                                                             @RequestParam("brandId") Long brandId,
                                                             @Parameter(description = "id продуктов")
                                                             @RequestParam("productId") List<Long> productIds) {

        brandService.addProductById(brandId, productIds);

        return ResponseEntity.ok(HttpStatus.OK);
    }
}
