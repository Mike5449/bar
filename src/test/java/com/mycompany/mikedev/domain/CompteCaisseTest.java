package com.mycompany.mikedev.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.mikedev.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompteCaisseTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteCaisse.class);
        CompteCaisse compteCaisse1 = new CompteCaisse();
        compteCaisse1.setId(1L);
        CompteCaisse compteCaisse2 = new CompteCaisse();
        compteCaisse2.setId(compteCaisse1.getId());
        assertThat(compteCaisse1).isEqualTo(compteCaisse2);
        compteCaisse2.setId(2L);
        assertThat(compteCaisse1).isNotEqualTo(compteCaisse2);
        compteCaisse1.setId(null);
        assertThat(compteCaisse1).isNotEqualTo(compteCaisse2);
    }
}
