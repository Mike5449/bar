package com.mycompany.mikedev.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DepotMapperTest {

    private DepotMapper depotMapper;

    @BeforeEach
    public void setUp() {
        depotMapper = new DepotMapperImpl();
    }
}
