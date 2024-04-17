package by.youngliqui.EShopProject.servicesImpl;

import by.youngliqui.EShopProject.dto.ProductDTO;
import by.youngliqui.EShopProject.exceptions.BrandNotFoundException;
import by.youngliqui.EShopProject.exceptions.CategoryNotFoundException;
import by.youngliqui.EShopProject.exceptions.ProductNotFoundException;
import by.youngliqui.EShopProject.mappers.ProductMapper;
import by.youngliqui.EShopProject.models.Bucket;
import by.youngliqui.EShopProject.models.Product;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.repositories.BrandRepository;
import by.youngliqui.EShopProject.repositories.CategoryRepository;
import by.youngliqui.EShopProject.repositories.ProductRepository;
import by.youngliqui.EShopProject.services.BucketService;
import by.youngliqui.EShopProject.services.ProductService;
import by.youngliqui.EShopProject.services.UserService;
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
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserService userService, BucketService bucketService, BrandRepository brandRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.bucketService = bucketService;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
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

        subtractQuantity(1, productId);
    }

    @Override
    @Transactional
    public void addProduct(ProductDTO productDTO) {
        if (productDTO.getAvailability() == null) {
            productDTO.setAvailability(false);
        }

        Product product = productMapper.toProduct(productDTO);

        String brandName = productDTO.getBrandName();
        if (brandName != null) {
            product.setBrand(brandRepository.findFirstByNameIgnoreCase(brandName).orElseThrow(
                    () -> new BrandNotFoundException("brand with name " + brandName + " was not found"))
            );
        }

        List<String> categoriesNames = productDTO.getCategoriesNames();
        if (!categoriesNames.isEmpty()) {
            for (String category : categoriesNames) {
                product.addCategory(categoryRepository.findFirstByTitleIgnoreCase(category).orElseThrow(
                        () -> new CategoryNotFoundException("category with title " + category + " was not found")
                ));
            }
        }

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

    @Override
    @Transactional
    public void addQuantity(Integer quantity, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("product with id " + productId + " was not found")
        );

        product.addQuantity(quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void subtractQuantity(Integer quantity, Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("product with id " + productId + " was not found")
        );

        Integer productQuantity = product.getQuantity();

        if (productQuantity < quantity) {
            throw new IllegalArgumentException("the quantity transferred is more than the quantity of the product");
        }

        if (productQuantity.equals(quantity)) {
            product.setUnavailable();
        }

        product.subtractQuantity(quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void setAvailable(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("product with id " + productId + " was not found")
        );

        if (product.getQuantity() <= 0) {
            throw new RuntimeException("quantity must be greater than 0");
        }

        product.setAvailable();
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void setUnavailable(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("product with id " + productId + " was not found")
        );

        product.setUnavailable();
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void addTagsToProduct(Long productId, List<String> tags) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("product with id " + productId + " was not found")
        );

        product.addTags(tags);
        productRepository.save(product);
    }
}
