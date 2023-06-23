package com.mycompany.mikedev.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.mikedev.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CompteCaisseDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompteCaisseDTO.class);
        CompteCaisseDTO compteCaisseDTO1 = new CompteCaisseDTO();
        compteCaisseDTO1.setId(1L);
        CompteCaisseDTO compteCaisseDTO2 = new CompteCaisseDTO();
        assertThat(compteCaisseDTO1).isNotEqualTo(compteCaisseDTO2);
        compteCaisseDTO2.setId(compteCaisseDTO1.getId());
        assertThat(compteCaisseDTO1).isEqualTo(compteCaisseDTO2);
        compteCaisseDTO2.setId(2L);
        assertThat(compteCaisseDTO1).isNotEqualTo(compteCaisseDTO2);
        compteCaisseDTO1.setId(null);
        assertThat(compteCaisseDTO1).isNotEqualTo(compteCaisseDTO2);
    }
}
