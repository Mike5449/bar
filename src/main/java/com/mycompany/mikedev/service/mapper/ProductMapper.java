package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Product;
import com.mycompany.mikedev.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {}
