package com.mycompany.mikedev.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.mikedev.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrixBoissonTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrixBoisson.class);
        PrixBoisson prixBoisson1 = new PrixBoisson();
        prixBoisson1.setId(1L);
        PrixBoisson prixBoisson2 = new PrixBoisson();
        prixBoisson2.setId(prixBoisson1.getId());
        assertThat(prixBoisson1).isEqualTo(prixBoisson2);
        prixBoisson2.setId(2L);
        assertThat(prixBoisson1).isNotEqualTo(prixBoisson2);
        prixBoisson1.setId(null);
        assertThat(prixBoisson1).isNotEqualTo(prixBoisson2);
    }
}
