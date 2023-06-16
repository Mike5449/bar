package com.mycompany.mikedev.service.impl;

import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.domain.Sale;
import com.mycompany.mikedev.domain.Stock;
import com.mycompany.mikedev.repository.SaleRepository;
import com.mycompany.mikedev.repository.StockRepository;
import com.mycompany.mikedev.service.SaleService;
import com.mycompany.mikedev.service.TokenManager;
import com.mycompany.mikedev.service.dto.SaleDTO;
import com.mycompany.mikedev.service.mapper.SaleMapper;
// import com.mycompany.mikedev.util.DateUtil;
import com.mycompany.mikedev.util.DateUtil;

import antlr.Token;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Sale}.
 */
@Service
@Transactional
public class SaleServiceImpl implements SaleService {

    private final Logger log = LoggerFactory.getLogger(SaleServiceImpl.class);

    private final SaleRepository saleRepository;

    private final SaleMapper saleMapper;

    private final StockRepository stockRepository;
    

    public SaleServiceImpl(SaleRepository saleRepository, SaleMapper saleMapper , StockRepository stockRepository ) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
        this.stockRepository=stockRepository;
        
    }

    @Override
    public SaleDTO save(SaleDTO saleDTO) {

        Sale sale=saleMapper.toEntity(saleDTO);
        Optional<Stock> stockOptional=stockRepository.findByProduct(sale.getProduct());

        if(stockOptional.isPresent()){

            Stock stock = stockOptional.get();
            int quantityVendu=sale.getQuantity();
            int quantityDispo=stock.getQuantity();

            if(quantityVendu <= quantityDispo){

                stock.setQuantity(quantityDispo - quantityVendu);
                stockRepository.save(stock);

                sale = saleRepository.save(sale);
                sale.setEmployee(new Employee(TokenManager.getSubject().getEmployeeId()));

                
            } else {

                throw new IllegalArgumentException("Quantite insuffisant en stock");
            }

        } else {

                throw new NoSuchElementException("Stock introuvable pour le produit specifie ."  );

        }

        

         
        return saleMapper.toDto(sale);
    }


    // @Override
    // public SaleDTO saves(SaleDTO saleDTO) {
    //     log.debug("Request to save Sale : {}", saleDTO);
    //     Sale sale = saleMapper.toEntity(saleDTO);
    //     sale = saleRepository.save(sale);
    //     sale.setEmployee(new Employee(TokenManager.getSubject().getEmployeeId()));
    //     return saleMapper.toDto(sale);
    // }

    @Override
    public SaleDTO update(SaleDTO saleDTO) {
        log.debug("Request to update Sale : {}", saleDTO);
        Sale sale = saleMapper.toEntity(saleDTO);
        sale = saleRepository.save(sale);
        return saleMapper.toDto(sale);
    }

    @Override
    public Optional<SaleDTO> partialUpdate(SaleDTO saleDTO) {
        log.debug("Request to partially update Sale : {}", saleDTO);

        return saleRepository
            .findById(saleDTO.getId())
            .map(existingSale -> {
                saleMapper.partialUpdate(existingSale, saleDTO);

                return existingSale;
            })
            .map(saleRepository::save)
            .map(saleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SaleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Sales");
        if(TokenManager.getSubject().getJobName().equals("serveur(se)")){
        return saleRepository.findAll(pageable).map(saleMapper::toDto);

        }
        return saleRepository.findAll(pageable).map(saleMapper::toDto);
    }
    @Override
    public List<SaleDTO> prendreParDateAndEmploy() {
        log.debug("Request to get all Sales"); 
        if(TokenManager.getSubject().getJobName().equals("Serveur(se)")){
        return saleMapper.toDto(saleRepository.prendreParDateAndEmploy(TokenManager.getSubject().getEmployeeId(),DateUtil.getDateInString(DateUtil.getCurrentDate(), "-")));

        }
       
        return saleMapper.toDto(saleRepository.prendreVenteAujourdhuit(DateUtil.getDateInString(DateUtil.getCurrentDate(), "-")));
    }

    public Page<SaleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return saleRepository.findAllWithEagerRelationships(pageable).map(saleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SaleDTO> findOne(Long id) {
        log.debug("Request to get Sale : {}", id);
        return saleRepository.findOneWithEagerRelationships(id).map(saleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Sale : {}", id);
        saleRepository.deleteById(id);
    }
}
