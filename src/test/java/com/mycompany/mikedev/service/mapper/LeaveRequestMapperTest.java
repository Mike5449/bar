package com.mycompany.mikedev.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LeaveRequestMapperTest {

    private LeaveRequestMapper leaveRequestMapper;

    @BeforeEach
    public void setUp() {
        leaveRequestMapper = new LeaveRequestMapperImpl();
    }
}
