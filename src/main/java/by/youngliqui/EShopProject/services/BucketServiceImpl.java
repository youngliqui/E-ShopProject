package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.dto.BucketDTO;
import by.youngliqui.EShopProject.dto.BucketDetailsDTO;
import by.youngliqui.EShopProject.models.Bucket;
import by.youngliqui.EShopProject.models.Product;
import by.youngliqui.EShopProject.models.User;
import by.youngliqui.EShopProject.repositories.BucketRepository;
import by.youngliqui.EShopProject.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class BucketServiceImpl implements BucketService {
    private final BucketRepository bucketRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    @Autowired
    public BucketServiceImpl(BucketRepository bucketRepository, ProductRepository productRepository, UserService userService) {
        this.bucketRepository = bucketRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Bucket createBucket(User user, List<Long> productsIds) {
        Bucket bucket = new Bucket();
        bucket.setUser(user);
        List<Product> productList = getCollectRefProductsByIds(productsIds);
        bucket.setProducts(productList);
        return bucketRepository.save(bucket);
    }

    private List<Product> getCollectRefProductsByIds(List<Long> productsIds) {
        return productsIds.stream()
                .map(id -> productRepository.findById(id).get())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addProducts(Bucket bucket, List<Long> productsIds) {
        List<Product> products = bucket.getProducts();
        List<Product> newProductList = products == null ? new ArrayList<>() : new ArrayList<>(products);
        newProductList.addAll(getCollectRefProductsByIds(productsIds));
        bucket.setProducts(newProductList);
        bucketRepository.save(bucket);
    }

    @Override
    public BucketDTO getBucketByUser(String name) {
        User user = userService.findByName(name);
        if (user == null || user.getBucket() == null) {
            return new BucketDTO();
        }

        BucketDTO bucketDTO = new BucketDTO();
        Map<Long, BucketDetailsDTO> mapByProductId = new HashMap<>();

        List<Product> products = user.getBucket().getProducts();
        for (Product product : products) {
            BucketDetailsDTO detail = mapByProductId.get(product.getId());
            if (detail == null) {
                mapByProductId.put(product.getId(), new BucketDetailsDTO(product));
            } else {
                detail.setAmount(detail.getAmount().add(new BigDecimal("1.0")));
                detail.setSum(detail.getSum() + Double.parseDouble(product.getPrice().toString()));
            }
        }

        bucketDTO.setBucketDetails(new ArrayList<>(mapByProductId.values()));
        bucketDTO.aggregate();

        return bucketDTO;
    }
}
