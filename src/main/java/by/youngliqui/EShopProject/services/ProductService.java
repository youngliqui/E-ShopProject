package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();
    void addToUserBucket(Long productId, String username);
    void addProduct(ProductDTO productDTO);
}
