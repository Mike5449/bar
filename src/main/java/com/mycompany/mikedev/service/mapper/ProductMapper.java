package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Product;
import com.mycompany.mikedev.domain.ProductPrice;
import com.mycompany.mikedev.service.dto.ProductDTO;
import com.mycompany.mikedev.service.dto.ProductPriceDTO;

import java.util.Set;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {

    // @Mapping(target = "productPrice", source = "productPrice", qualifiedByName = "productPriceId")
    // ProductDTO toDto(Product p);

    // @Named("productPriceId")
    // @Mapping(target = "id", source = "id")
    // @Mapping(target = "price", source = "price")
    // ProductPriceDTO toDtoProductPrice(ProductPrice price);

}
