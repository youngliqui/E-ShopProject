package by.youngliqui.EShopProject.mappers;

import by.youngliqui.EShopProject.dto.ProductDTO;
import by.youngliqui.EShopProject.models.Brand;
import by.youngliqui.EShopProject.models.Category;
import by.youngliqui.EShopProject.models.Product;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface ProductMapper {
    ProductMapper MAPPER = Mappers.getMapper(ProductMapper.class);

    Product toProduct(ProductDTO productDTO);

    @InheritInverseConfiguration
    @Mapping(target = "categoriesNames", source = "categories", qualifiedByName = "mapCategoriesToNames")
    @Mapping(target = "brandName", source = "brand", qualifiedByName = "mapBrandToName")
    ProductDTO fromProduct(Product product);

    List<Product> toProductList(List<ProductDTO> productDTOS);

    List<ProductDTO> fromProductList(List<Product> productList);


    @Named("mapCategoriesToNames")
    default List<String> mapCategoriesToNames(List<Category> categories) {
        return categories.stream().map(Category::getTitle).collect(Collectors.toList());
    }

    @Named("mapBrandToName")
    default String mapBrandToName(Brand brand) {
        return brand != null ? brand.getName() : null;
    }
}
