package com.mycompany.mikedev.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProductPriceMapperTest {

    private ProductPriceMapper productPriceMapper;

    @BeforeEach
    public void setUp() {
        productPriceMapper = new ProductPriceMapperImpl();
    }
}
