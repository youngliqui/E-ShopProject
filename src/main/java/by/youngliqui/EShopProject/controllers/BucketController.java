package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.BucketDTO;
import by.youngliqui.EShopProject.services.BucketService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/bucket")
@Tag(name = "Корзина товаров", description = "методы для работы с корзиной товаров")
@SecurityRequirement(name = "basicAuth")
public class BucketController {
    private final BucketService bucketService;

    @Autowired
    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @GetMapping
    @Operation(summary = "получение корзины пользователя")
    public BucketDTO aboutBucket(Principal principal) {
        if (principal == null) {
            return new BucketDTO();
        } else {
            return bucketService.getBucketByUser(principal.getName());
        }
    }

    @PostMapping
    @Operation(summary = "подтверждение корзины пользователя")
    public ResponseEntity<HttpStatus> commitBucket(Principal principal) {
        if (principal != null) {
            bucketService.commitBucketToOrder(principal.getName());
            return ResponseEntity.ok(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
