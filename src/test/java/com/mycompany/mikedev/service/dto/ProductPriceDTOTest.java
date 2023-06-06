package com.mycompany.mikedev.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.mikedev.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProductPriceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductPriceDTO.class);
        ProductPriceDTO productPriceDTO1 = new ProductPriceDTO();
        productPriceDTO1.setId(1L);
        ProductPriceDTO productPriceDTO2 = new ProductPriceDTO();
        assertThat(productPriceDTO1).isNotEqualTo(productPriceDTO2);
        productPriceDTO2.setId(productPriceDTO1.getId());
        assertThat(productPriceDTO1).isEqualTo(productPriceDTO2);
        productPriceDTO2.setId(2L);
        assertThat(productPriceDTO1).isNotEqualTo(productPriceDTO2);
        productPriceDTO1.setId(null);
        assertThat(productPriceDTO1).isNotEqualTo(productPriceDTO2);
    }
}
