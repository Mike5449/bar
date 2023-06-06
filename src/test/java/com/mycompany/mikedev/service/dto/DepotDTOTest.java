package com.mycompany.mikedev.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.mikedev.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class DepotDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(DepotDTO.class);
        DepotDTO depotDTO1 = new DepotDTO();
        depotDTO1.setId(1L);
        DepotDTO depotDTO2 = new DepotDTO();
        assertThat(depotDTO1).isNotEqualTo(depotDTO2);
        depotDTO2.setId(depotDTO1.getId());
        assertThat(depotDTO1).isEqualTo(depotDTO2);
        depotDTO2.setId(2L);
        assertThat(depotDTO1).isNotEqualTo(depotDTO2);
        depotDTO1.setId(null);
        assertThat(depotDTO1).isNotEqualTo(depotDTO2);
    }
}
