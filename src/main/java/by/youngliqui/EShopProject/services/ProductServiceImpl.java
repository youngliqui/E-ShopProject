package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.ProductDTO;
import by.youngliqui.EShopProject.exceptions.ProductNotFoundException;
import by.youngliqui.EShopProject.mappers.ProductMapper;
import by.youngliqui.EShopProject.models.Bucket;
import by.youngliqui.EShopProject.models.Product;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper = ProductMapper.MAPPER;

    private final UserService userService;
    private final BucketService bucketService;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserService userService, BucketService bucketService) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.bucketService = bucketService;
    }

    @Override
    public List<ProductDTO> getAll() {
        return productMapper.fromProductList(productRepository.findAll());
    }

    @Override
    @Transactional
    public void addToUserBucket(Long productId, String username) {
        User user = userService.findByName(username);
        if (user == null) {
            throw new UsernameNotFoundException("user not found with name: " + username);
        }

        Bucket bucket = user.getBucket();
        if (bucket == null) {
            Bucket newBucket = bucketService.createBucket(user, Collections.singletonList(productId));
            user.setBucket(newBucket);
            userService.save(user);
        } else {
            bucketService.addProducts(bucket, Collections.singletonList(productId));
        }
    }

    @Override
    @Transactional
    public void addProduct(ProductDTO productDTO) {
        Product product = productMapper.toProduct(productDTO);
        productRepository.save(product);
    }

    @Override
    public ProductDTO getById(Long id) {
        Product product = productRepository.findById(id).orElseThrow(
                () -> new ProductNotFoundException("product with id = " + id + " was not found"));
        return ProductMapper.MAPPER.fromProduct(product);
    }

    @Override
    @Transactional
    public void deleteProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("product with id " + productId + " was not found"));

        productRepository.delete(product);
    }

    @Override
    public List<ProductDTO> getFilteredAndSortedProducts(
            String title, String sortBy, int page, int pageSize) {

        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<Product> productsPage;

        if (title != null) {
            if (sortBy.equals("priceAsc")) {
                productsPage = productRepository.findAllByTitleContainingIgnoreCaseOrderByPriceAsc(title, pageRequest);
            } else if (sortBy.equals("priceDesc")) {
                productsPage = productRepository.findAllByTitleContainingIgnoreCaseOrderByPriceDesc(title, pageRequest);
            } else {
                productsPage = productRepository.findAllByTitleContainingIgnoreCase(title, pageRequest);
            }
        } else {
            if (sortBy.equals("priceAsc")) {
                productsPage = productRepository.findAllSortedByAscendingPrice(pageRequest);
            } else if (sortBy.equals("priceDesc")) {
                productsPage = productRepository.findAllSortedByDescendingPrice(pageRequest);
            } else {
                productsPage = productRepository.findAll(pageRequest);
            }
        }


        return productMapper.fromProductList(productsPage.getContent());
    }
}
