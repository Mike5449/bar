package com.mycompany.mikedev.service.impl;

import com.mycompany.mikedev.domain.CompteCaisse;
import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.domain.Sale;
import com.mycompany.mikedev.domain.Stock;
import com.mycompany.mikedev.domain.enumeration.StatusCaisse;
import com.mycompany.mikedev.domain.enumeration.StatusVente;
import com.mycompany.mikedev.repository.CompteCaisseRepository;
import com.mycompany.mikedev.repository.SaleRepository;
import com.mycompany.mikedev.repository.StockRepository;
import com.mycompany.mikedev.service.SaleService;
import com.mycompany.mikedev.service.TokenManager;
import com.mycompany.mikedev.service.dto.CompteCaisseDTO;
import com.mycompany.mikedev.service.dto.SaleDTO;
import com.mycompany.mikedev.service.mapper.CompteCaisseMapper;
import com.mycompany.mikedev.service.mapper.SaleMapper;
// import com.mycompany.mikedev.util.DateUtil;
import com.mycompany.mikedev.util.DateUtil;

import antlr.Token;
import net.bytebuddy.asm.Advice.OffsetMapping.Target.ForArray.ReadOnly;

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

    private final CompteCaisseMapper compteCaisseMapper;

    private final StockRepository stockRepository;

    private final CompteCaisseRepository compteCaisseRepository;
    

    public SaleServiceImpl(SaleRepository saleRepository, SaleMapper saleMapper , StockRepository stockRepository ,CompteCaisseRepository compteCaisseRepository , CompteCaisseMapper compteCaisseMapper) {
        this.saleRepository = saleRepository;
        this.saleMapper = saleMapper;
        this.stockRepository=stockRepository;
        this.compteCaisseRepository=compteCaisseRepository;
        this.compteCaisseMapper=compteCaisseMapper;
        
    }

    @Override
    public SaleDTO save(SaleDTO saleDTO) {

        Sale sale=saleMapper.toEntity(saleDTO);
        Optional<Stock> stockOptional=stockRepository.findByProduct(sale.getProduct());


        // Long saleCaisse=compteCaisses.get().getSale() + sale.getQuantity()*sale.getProduct().getPrice();


        if(stockOptional.isPresent()){

            Stock stock = stockOptional.get();
            int quantityVendu=sale.getQuantity();
            int quantityDispo=stock.getQuantity();

            if(quantityVendu <= quantityDispo){

                // stock.setQuantity(quantityDispo - quantityVendu);
                // stockRepository.save(stock);
                //     sale = saleRepository.save(sale);
                // sale.setEmployee(new Employee(TokenManager.getSubject().getEmployeeId()));
                 

                
                if(sale.getEmployee()!=null){

                 sale = saleRepository.save(sale);

                }else{

                 sale.setEmployee(new Employee(TokenManager.getSubject().getEmployeeId()));
                 sale = saleRepository.save(sale);

                }

                
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
    // @Transactional(readOnly = true)
    public SaleDTO update(SaleDTO saleDTO) {
        log.debug("Request to update Sale : {}", saleDTO);
        
        Sale sale = saleMapper.toEntity(saleDTO);

        Optional<Sale> findIsValidate=saleRepository.findById(sale.getId());

        if(findIsValidate.isPresent()){

            if(sale.getStatus().equals(StatusVente.VALIDATE)){

                if(findIsValidate.get().getStatus().equals(StatusVente.NEW)){

                    Optional<Stock> stockOptional=stockRepository.findByProduct(sale.getProduct());

                    sale = saleRepository.save(sale);
        
                    int quantityVendu=sale.getQuantity();
        
                    List<CompteCaisse> compteCaisses=compteCaisseRepository.findByEmployeeAndCompteActive(TokenManager.getSubject().getEmployeeId(), StatusCaisse.ACTIVE);
                    Long productPrice=sale.getProduct().getPrice();
        
                    CompteCaisse caiss=compteCaisses.get(0);

                    Long saleCaisse=caiss.getSale() + quantityVendu*productPrice;
                    caiss.setSale(saleCaisse);

                    // getCash();

                    // Long cashCaisse=caiss.getCash() + caiss.getSale();
                    // caiss.setCash(cashCaisse);

                    // Long aVerserCaisse=caiss.getCash() + caiss.getBalance();
                    // caiss.setAVerser(aVerserCaisse);
                    
                    compteCaisseRepository.save(caiss);

                    Stock stock = stockOptional.get();
                    int quantityDispo=stock.getQuantity();
                    stock.setQuantity(quantityDispo - quantityVendu);
                    stockRepository.save(stock);
    
                } else if (findIsValidate.get().getStatus().equals(StatusVente.CANCEL)){
                   throw new IllegalArgumentException("Vous pouvez pas valider une commande qui est annulee");
                }else{
    
                   throw new IllegalArgumentException("Commande deja valide");
    
                }

            }else if(sale.getStatus().equals(StatusVente.CANCEL)){

                Optional<Stock> stockOptional=stockRepository.findByProduct(sale.getProduct());


                if(findIsValidate.get().getStatus().equals(StatusVente.VALIDATE)){

                    sale = saleRepository.save(sale);
            
                    int quantityVendu=sale.getQuantity();
        
                    List<CompteCaisse> compteCaisses=compteCaisseRepository.findByEmployeeAndCompteActive(TokenManager.getSubject().getEmployeeId(), StatusCaisse.ACTIVE);
                    Long productPrice=sale.getProduct().getPrice();
        
                    CompteCaisse caiss=compteCaisses.get(0);

                    Long cancelCaisse=caiss.getCancel() + quantityVendu*productPrice;
                    caiss.setCancel(cancelCaisse);

                    Stock stock = stockOptional.get();
                    int quantityDispo=stock.getQuantity();
                    stock.setQuantity(quantityDispo + quantityVendu);
                    stockRepository.save(stock);

                    // Long cashCaisse=caiss.getCash() - quantityVendu*productPrice;
                    // caiss.setCash(cashCaisse);

                    // Long saleCaisse=caiss.getSale() - quantityVendu*productPrice;
                    // caiss.setSale(saleCaisse);

                    // getCash();
                    
                    compteCaisseRepository.save(caiss);

                }

            }

            

        }

        
        


        return saleMapper.toDto(sale);
    }

    public CompteCaisseDTO getCash(){

        List<CompteCaisse> compteCaisses=compteCaisseRepository.findByEmployeeAndCompteActive(TokenManager.getSubject().getEmployeeId(), StatusCaisse.ACTIVE);       
        CompteCaisse caiss=compteCaisses.get(0);

        Long injection=caiss.getInjection();
        Long sale=caiss.getSale();
        Long pret =caiss.getPret();
        long cancel =caiss.getCancel();
        Long balance = caiss.getBalance();

        long cash=injection + sale - cancel - pret ;

        caiss.setCash(cash);
        caiss.setAVerser(cash + balance);

        compteCaisseRepository.save(caiss);
        return compteCaisseMapper.toDto(caiss);

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
