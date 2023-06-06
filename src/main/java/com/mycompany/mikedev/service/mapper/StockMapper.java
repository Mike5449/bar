package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Product;
import com.mycompany.mikedev.domain.Stock;
import com.mycompany.mikedev.service.dto.ProductDTO;
import com.mycompany.mikedev.service.dto.StockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Stock} and its DTO {@link StockDTO}.
 */
@Mapper(componentModel = "spring")
public interface StockMapper extends EntityMapper<StockDTO, Stock> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    StockDTO toDto(Stock s);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProductDTO toDtoProductName(Product product);
}
