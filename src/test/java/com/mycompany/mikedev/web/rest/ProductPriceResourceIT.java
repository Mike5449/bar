package com.mycompany.mikedev.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.mikedev.IntegrationTest;
import com.mycompany.mikedev.domain.ProductPrice;
import com.mycompany.mikedev.domain.enumeration.StatusPrice;
import com.mycompany.mikedev.repository.ProductPriceRepository;
import com.mycompany.mikedev.service.ProductPriceService;
import com.mycompany.mikedev.service.dto.ProductPriceDTO;
import com.mycompany.mikedev.service.mapper.ProductPriceMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProductPriceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProductPriceResourceIT {

    private static final Float DEFAULT_PRICE = 1F;
    private static final Float UPDATED_PRICE = 2F;

    private static final StatusPrice DEFAULT_STATUS = StatusPrice.CURRENT;
    private static final StatusPrice UPDATED_STATUS = StatusPrice.CLOSE;

    private static final String ENTITY_API_URL = "/api/product-prices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProductPriceRepository productPriceRepository;

    @Mock
    private ProductPriceRepository productPriceRepositoryMock;

    @Autowired
    private ProductPriceMapper productPriceMapper;

    @Mock
    private ProductPriceService productPriceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProductPriceMockMvc;

    private ProductPrice productPrice;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPrice createEntity(EntityManager em) {
        ProductPrice productPrice = new ProductPrice().price(DEFAULT_PRICE).status(DEFAULT_STATUS);
        return productPrice;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductPrice createUpdatedEntity(EntityManager em) {
        ProductPrice productPrice = new ProductPrice().price(UPDATED_PRICE).status(UPDATED_STATUS);
        return productPrice;
    }

    @BeforeEach
    public void initTest() {
        productPrice = createEntity(em);
    }

    @Test
    @Transactional
    void createProductPrice() throws Exception {
        int databaseSizeBeforeCreate = productPriceRepository.findAll().size();
        // Create the ProductPrice
        ProductPriceDTO productPriceDTO = productPriceMapper.toDto(productPrice);
        restProductPriceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productPriceDTO))
            )
            .andExpect(status().isCreated());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeCreate + 1);
        ProductPrice testProductPrice = productPriceList.get(productPriceList.size() - 1);
        assertThat(testProductPrice.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProductPrice.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createProductPriceWithExistingId() throws Exception {
        // Create the ProductPrice with an existing ID
        productPrice.setId(1L);
        ProductPriceDTO productPriceDTO = productPriceMapper.toDto(productPrice);

        int databaseSizeBeforeCreate = productPriceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductPriceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPriceIsRequired() throws Exception {
        int databaseSizeBeforeTest = productPriceRepository.findAll().size();
        // set the field null
        productPrice.setPrice(null);

        // Create the ProductPrice, which fails.
        ProductPriceDTO productPriceDTO = productPriceMapper.toDto(productPrice);

        restProductPriceMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productPriceDTO))
            )
            .andExpect(status().isBadRequest());

        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProductPrices() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        // Get all the productPriceList
        restProductPriceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productPrice.getId().intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductPricesWithEagerRelationshipsIsEnabled() throws Exception {
        when(productPriceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductPriceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(productPriceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProductPricesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(productPriceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProductPriceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(productPriceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProductPrice() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        // Get the productPrice
        restProductPriceMockMvc
            .perform(get(ENTITY_API_URL_ID, productPrice.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(productPrice.getId().intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProductPrice() throws Exception {
        // Get the productPrice
        restProductPriceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProductPrice() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();

        // Update the productPrice
        ProductPrice updatedProductPrice = productPriceRepository.findById(productPrice.getId()).get();
        // Disconnect from session so that the updates on updatedProductPrice are not directly saved in db
        em.detach(updatedProductPrice);
        updatedProductPrice.price(UPDATED_PRICE).status(UPDATED_STATUS);
        ProductPriceDTO productPriceDTO = productPriceMapper.toDto(updatedProductPrice);

        restProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productPriceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productPriceDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
        ProductPrice testProductPrice = productPriceList.get(productPriceList.size() - 1);
        assertThat(testProductPrice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProductPrice.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // Create the ProductPrice
        ProductPriceDTO productPriceDTO = productPriceMapper.toDto(productPrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, productPriceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // Create the ProductPrice
        ProductPriceDTO productPriceDTO = productPriceMapper.toDto(productPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(productPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // Create the ProductPrice
        ProductPriceDTO productPriceDTO = productPriceMapper.toDto(productPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(productPriceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProductPriceWithPatch() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();

        // Update the productPrice using partial update
        ProductPrice partialUpdatedProductPrice = new ProductPrice();
        partialUpdatedProductPrice.setId(productPrice.getId());

        partialUpdatedProductPrice.status(UPDATED_STATUS);

        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
        ProductPrice testProductPrice = productPriceList.get(productPriceList.size() - 1);
        assertThat(testProductPrice.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testProductPrice.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateProductPriceWithPatch() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();

        // Update the productPrice using partial update
        ProductPrice partialUpdatedProductPrice = new ProductPrice();
        partialUpdatedProductPrice.setId(productPrice.getId());

        partialUpdatedProductPrice.price(UPDATED_PRICE).status(UPDATED_STATUS);

        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProductPrice.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProductPrice))
            )
            .andExpect(status().isOk());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
        ProductPrice testProductPrice = productPriceList.get(productPriceList.size() - 1);
        assertThat(testProductPrice.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testProductPrice.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // Create the ProductPrice
        ProductPriceDTO productPriceDTO = productPriceMapper.toDto(productPrice);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, productPriceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // Create the ProductPrice
        ProductPriceDTO productPriceDTO = productPriceMapper.toDto(productPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productPriceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProductPrice() throws Exception {
        int databaseSizeBeforeUpdate = productPriceRepository.findAll().size();
        productPrice.setId(count.incrementAndGet());

        // Create the ProductPrice
        ProductPriceDTO productPriceDTO = productPriceMapper.toDto(productPrice);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProductPriceMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(productPriceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProductPrice in the database
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProductPrice() throws Exception {
        // Initialize the database
        productPriceRepository.saveAndFlush(productPrice);

        int databaseSizeBeforeDelete = productPriceRepository.findAll().size();

        // Delete the productPrice
        restProductPriceMockMvc
            .perform(delete(ENTITY_API_URL_ID, productPrice.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductPrice> productPriceList = productPriceRepository.findAll();
        assertThat(productPriceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
