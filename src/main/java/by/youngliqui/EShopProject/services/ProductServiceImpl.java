package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.ProductDTO;
import by.youngliqui.EShopProject.mappers.ProductMapper;
import by.youngliqui.EShopProject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final ProductMapper productMapper = ProductMapper.MAPPER;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAll() {
        return productMapper.fromProductList(productRepository.findAll());
    }
}
