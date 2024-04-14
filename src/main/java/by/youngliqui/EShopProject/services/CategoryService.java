package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.models.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAll();

    Category getById(Long id);

    Category getByName(String name);

    void save(Category category);

    void addCategoryToProductById(Long productId, Long categoryId);

    void addCategoryToProductByName(Long productId, String categoryName);

    void deleteCategoryById(Long id);
}
