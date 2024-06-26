package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.ProductDTO;

import java.util.List;

public interface ProductService {
    List<ProductDTO> getAll();

    void addToUserBucket(Long productId, String username);

    void addProduct(ProductDTO productDTO);

    ProductDTO getById(Long id);

    void deleteProductById(Long productId);

    List<ProductDTO> getFilteredAndSortedProducts(String title, String sortBy, int page, int pageSize);

    void addQuantity(Integer quantity, Long productId);

    void subtractQuantity(Integer quantity, Long productId);

    void setAvailable(Long productId);

    void setUnavailable(Long productId);

    void addTagsToProduct(Long productId, List<String> tags);
}
