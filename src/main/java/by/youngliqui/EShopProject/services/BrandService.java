package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.BrandDTO;
import by.youngliqui.EShopProject.models.Brand;

import java.util.List;

public interface BrandService {
    void save(BrandDTO brandDTO);

    void save(Brand brand);

    List<BrandDTO> getAll();

    BrandDTO findByName(String name);

    BrandDTO findById(Long id);

    void deleteById(Long id);

    void deleteByUsername(String username);

    void addProductById(Long brandId, List<Long> productId);
}
