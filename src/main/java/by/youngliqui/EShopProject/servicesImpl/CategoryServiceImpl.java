package by.youngliqui.EShopProject.servicesImpl;

import by.youngliqui.EShopProject.exceptions.CategoryNotFoundException;
import by.youngliqui.EShopProject.exceptions.ProductNotFoundException;
import by.youngliqui.EShopProject.models.Category;
import by.youngliqui.EShopProject.models.Product;
import by.youngliqui.EShopProject.repositories.CategoryRepository;
import by.youngliqui.EShopProject.repositories.ProductRepository;
import by.youngliqui.EShopProject.services.CategoryService;
import by.youngliqui.EShopProject.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Component
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProductService productService, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getById(Long id) {
        return categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("category with id " + id + " was not found")
        );
    }

    @Override
    public Category getByName(String name) {
        return categoryRepository.findFirstByTitleContainingIgnoreCase(name).orElseThrow(
                () -> new CategoryNotFoundException("category with name " + name + " was not found")
        );
    }

    @Override
    @Transactional
    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void addCategoryToProductById(Long productId, Long categoryId) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("product with id " + productId + " was not found")
        );

        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new CategoryNotFoundException("category with id " + categoryId + " was not found")
        );

        product.addCategory(category);
        productRepository.save(product);
    }

    @Override
    public void addCategoryToProductByName(Long productId, String categoryName) {
        Product product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("product with id " + productId + " was not found")
        );

        Category category = categoryRepository.findFirstByTitleContainingIgnoreCase(categoryName).orElseThrow(
                () -> new CategoryNotFoundException("category with name " + categoryName + " not found")
        );

        product.addCategory(category);
        productRepository.save(product);
    }

    @Override
    public void deleteCategoryById(Long id) {
        Category deletedCategory = categoryRepository.findById(id).orElseThrow(
                () -> new CategoryNotFoundException("category with id " + id + " was not found")
        );

        categoryRepository.delete(deletedCategory);
    }
}
