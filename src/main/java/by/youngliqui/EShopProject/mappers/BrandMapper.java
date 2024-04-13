package by.youngliqui.EShopProject.mappers;

import by.youngliqui.EShopProject.dto.BrandDTO;
import by.youngliqui.EShopProject.models.Brand;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BrandMapper {
    BrandMapper MAPPER = Mappers.getMapper(BrandMapper.class);

    Brand toBrand(BrandDTO brandDTO);

    @InheritInverseConfiguration
    BrandDTO fromBrand(Brand brand);

    List<Brand> toBrandList(List<BrandDTO> brandDTOS);

    List<BrandDTO> fromBrandList(List<Brand> brands);
}
