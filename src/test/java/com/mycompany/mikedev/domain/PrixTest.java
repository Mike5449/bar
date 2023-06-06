package com.mycompany.mikedev.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.mikedev.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrixTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Prix.class);
        Prix prix1 = new Prix();
        prix1.setId(1L);
        Prix prix2 = new Prix();
        prix2.setId(prix1.getId());
        assertThat(prix1).isEqualTo(prix2);
        prix2.setId(2L);
        assertThat(prix1).isNotEqualTo(prix2);
        prix1.setId(null);
        assertThat(prix1).isNotEqualTo(prix2);
    }
}
