package by.youngliqui.EShopProject.servicesImpl;

import by.youngliqui.EShopProject.dto.BrandDTO;
import by.youngliqui.EShopProject.exceptions.BrandNotFoundException;
import by.youngliqui.EShopProject.models.Brand;
import by.youngliqui.EShopProject.models.Product;
import by.youngliqui.EShopProject.repositories.BrandRepository;
import by.youngliqui.EShopProject.repositories.ProductRepository;
import by.youngliqui.EShopProject.services.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static by.youngliqui.EShopProject.mappers.BrandMapper.MAPPER;

@Service
@Component
@Transactional(readOnly = true)
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;

    private final ProductRepository productRepository;

    @Autowired
    public BrandServiceImpl(BrandRepository brandRepository, ProductRepository productRepository) {
        this.brandRepository = brandRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public void save(BrandDTO brandDTO) {
        Brand brand = Brand.builder()
                .description(brandDTO.getDescription())
                .name(brandDTO.getName())
                .build();

        brandRepository.save(brand);
    }

    @Override
    @Transactional
    public void save(Brand brand) {
        brandRepository.save(brand);
    }

    @Override
    public List<BrandDTO> getAll() {
        return MAPPER.fromBrandList(brandRepository.findAll());
    }

    @Override
    public BrandDTO findByName(String name) {
        return MAPPER.fromBrand(brandRepository.findFirstByNameContainingIgnoreCase(name)
                .orElseThrow(() -> new BrandNotFoundException("brand with name " + name + " was not found"))
        );
    }

    @Override
    public BrandDTO findById(Long id) {
        return MAPPER.fromBrand(brandRepository.findById(id).orElseThrow(
                () -> new BrandNotFoundException("brand with id " + id + " was not found")
        ));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Brand deletedBrand = brandRepository.findById(id).orElseThrow(
                () -> new BrandNotFoundException("brand with id " + id + " was not found")
        );

        brandRepository.delete(deletedBrand);
    }

    @Override
    @Transactional
    public void deleteByUsername(String username) {
        Brand deletedBrand = brandRepository.findFirstByName(username).orElseThrow(
                () -> new BrandNotFoundException("brand with name " + username + " was not found")
        );

        brandRepository.delete(deletedBrand);
    }

    @Override
    @Transactional
    public void addProductById(Long brandId, List<Long> productIds) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new BrandNotFoundException("brand with id " + brandId + " was not found"));

        List<Product> products = productRepository.findAllById(productIds);
        brand.addProducts(products);

        for (Product product : products) {
            product.addBrand(brand);
        }

        productRepository.saveAll(products);
        brandRepository.save(brand);
    }
}
