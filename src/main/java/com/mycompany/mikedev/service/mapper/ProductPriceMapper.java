package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Product;
import com.mycompany.mikedev.domain.ProductPrice;
import com.mycompany.mikedev.service.dto.ProductDTO;
import com.mycompany.mikedev.service.dto.ProductPriceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductPrice} and its DTO {@link ProductPriceDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductPriceMapper extends EntityMapper<ProductPriceDTO, ProductPrice> {
    @Mapping(target = "produit", source = "produit", qualifiedByName = "productName")
    ProductPriceDTO toDto(ProductPrice s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
