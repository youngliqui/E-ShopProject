package by.youngliqui.EShopProject.dto;

import java.util.List;

public class ProductsResponse {
    private List<ProductDTO> products;

    public ProductsResponse(List<ProductDTO> products) {
        this.products = products;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(List<ProductDTO> products) {
        this.products = products;
    }
}
