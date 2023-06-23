package com.mycompany.mikedev.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompteCaisseMapperTest {

    private CompteCaisseMapper compteCaisseMapper;

    @BeforeEach
    public void setUp() {
        compteCaisseMapper = new CompteCaisseMapperImpl();
    }
}
