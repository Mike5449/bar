package com.mycompany.mikedev.service.impl;

import com.mycompany.mikedev.domain.ProductPrice;
import com.mycompany.mikedev.repository.ProductPriceRepository;
import com.mycompany.mikedev.service.ProductPriceService;
import com.mycompany.mikedev.service.dto.ProductPriceDTO;
import com.mycompany.mikedev.service.mapper.ProductPriceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ProductPrice}.
 */
@Service
@Transactional
public class ProductPriceServiceImpl implements ProductPriceService {

    private final Logger log = LoggerFactory.getLogger(ProductPriceServiceImpl.class);

    private final ProductPriceRepository productPriceRepository;

    private final ProductPriceMapper productPriceMapper;

    public ProductPriceServiceImpl(ProductPriceRepository productPriceRepository, ProductPriceMapper productPriceMapper) {
        this.productPriceRepository = productPriceRepository;
        this.productPriceMapper = productPriceMapper;
    }

    @Override
    public ProductPriceDTO save(ProductPriceDTO productPriceDTO) {
        log.debug("Request to save ProductPrice : {}", productPriceDTO);
        ProductPrice productPrice = productPriceMapper.toEntity(productPriceDTO);
        productPrice = productPriceRepository.save(productPrice);
        return productPriceMapper.toDto(productPrice);
    }

    @Override
    public ProductPriceDTO update(ProductPriceDTO productPriceDTO) {
        log.debug("Request to update ProductPrice : {}", productPriceDTO);
        ProductPrice productPrice = productPriceMapper.toEntity(productPriceDTO);
        productPrice = productPriceRepository.save(productPrice);
        return productPriceMapper.toDto(productPrice);
    }

    @Override
    public Optional<ProductPriceDTO> partialUpdate(ProductPriceDTO productPriceDTO) {
        log.debug("Request to partially update ProductPrice : {}", productPriceDTO);

        return productPriceRepository
            .findById(productPriceDTO.getId())
            .map(existingProductPrice -> {
                productPriceMapper.partialUpdate(existingProductPrice, productPriceDTO);

                return existingProductPrice;
            })
            .map(productPriceRepository::save)
            .map(productPriceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductPriceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ProductPrices");
        return productPriceRepository.findAll(pageable).map(productPriceMapper::toDto);
    }

    public Page<ProductPriceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return productPriceRepository.findAllWithEagerRelationships(pageable).map(productPriceMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductPriceDTO> findOne(Long id) {
        log.debug("Request to get ProductPrice : {}", id);
        return productPriceRepository.findOneWithEagerRelationships(id).map(productPriceMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ProductPrice : {}", id);
        productPriceRepository.deleteById(id);
    }
}
