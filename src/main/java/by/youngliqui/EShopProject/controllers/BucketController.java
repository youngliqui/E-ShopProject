package by.youngliqui.EShopProject.controllers;

import by.youngliqui.EShopProject.dto.BucketDTO;
import by.youngliqui.EShopProject.services.BucketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/bucket")
public class BucketController {
    private final BucketService bucketService;

    @Autowired
    public BucketController(BucketService bucketService) {
        this.bucketService = bucketService;
    }

    @GetMapping
    public BucketDTO aboutBucket(Principal principal) {
        if (principal == null) {
            return new BucketDTO();
        } else {
            return bucketService.getBucketByUser(principal.getName());
        }
    }
}
