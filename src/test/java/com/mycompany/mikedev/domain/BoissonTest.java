package com.mycompany.mikedev.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.mikedev.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BoissonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Boisson.class);
        Boisson boisson1 = new Boisson();
        boisson1.setId(1L);
        Boisson boisson2 = new Boisson();
        boisson2.setId(boisson1.getId());
        assertThat(boisson1).isEqualTo(boisson2);
        boisson2.setId(2L);
        assertThat(boisson1).isNotEqualTo(boisson2);
        boisson1.setId(null);
        assertThat(boisson1).isNotEqualTo(boisson2);
    }
}
