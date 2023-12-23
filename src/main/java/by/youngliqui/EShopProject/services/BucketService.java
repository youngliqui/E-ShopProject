package by.youngliqui.EShopProject.services;

import by.youngliqui.EShopProject.models.Bucket;
import by.youngliqui.EShopProject.models.User;

import java.util.List;

public interface BucketService {
    Bucket createBucket(User user, List<Long> productsIds);

    void addProducts(Bucket bucket, List<Long> productsIds);
}
