package com.mycompany.mikedev.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalaryMapperTest {

    private SalaryMapper salaryMapper;

    @BeforeEach
    public void setUp() {
        salaryMapper = new SalaryMapperImpl();
    }
}
