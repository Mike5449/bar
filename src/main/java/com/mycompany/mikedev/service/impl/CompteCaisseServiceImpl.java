package com.mycompany.mikedev.service.impl;

import com.mycompany.mikedev.domain.CompteCaisse;
import com.mycompany.mikedev.domain.enumeration.StatusCaisse;
import com.mycompany.mikedev.repository.CompteCaisseRepository;
import com.mycompany.mikedev.service.CompteCaisseService;
import com.mycompany.mikedev.service.TokenManager;
import com.mycompany.mikedev.service.dto.CompteCaisseDTO;
import com.mycompany.mikedev.service.mapper.CompteCaisseMapper;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CompteCaisse}.
 */
@Service
@Transactional
public class CompteCaisseServiceImpl implements CompteCaisseService {

    private final Logger log = LoggerFactory.getLogger(CompteCaisseServiceImpl.class);

    private final CompteCaisseRepository compteCaisseRepository;

    private final CompteCaisseMapper compteCaisseMapper;

    public CompteCaisseServiceImpl(CompteCaisseRepository compteCaisseRepository, CompteCaisseMapper compteCaisseMapper) {
        this.compteCaisseRepository = compteCaisseRepository;
        this.compteCaisseMapper = compteCaisseMapper;
    }

    @Override
    public CompteCaisseDTO save(CompteCaisseDTO compteCaisseDTO) {
        log.debug("Request to save CompteCaisse : {}", compteCaisseDTO);
        CompteCaisse compteCaisse = compteCaisseMapper.toEntity(compteCaisseDTO);
        compteCaisse = compteCaisseRepository.save(compteCaisse);
        return compteCaisseMapper.toDto(compteCaisse);
    }

    @Override
    public CompteCaisseDTO update(CompteCaisseDTO compteCaisseDTO) {
        log.debug("Request to update CompteCaisse : {}", compteCaisseDTO);
        CompteCaisse compteCaisse = compteCaisseMapper.toEntity(compteCaisseDTO);
        compteCaisse = compteCaisseRepository.save(compteCaisse);
        return compteCaisseMapper.toDto(compteCaisse);
    }

    @Override
    public CompteCaisseDTO updateInjection(CompteCaisseDTO compteCaisseDTO , Long montant) {
        log.debug("Request to update CompteCaisse : {}", compteCaisseDTO);

        List<CompteCaisse> compteCaisses=compteCaisseRepository.findByEmployeeAndCompteActive(TokenManager.getSubject().getEmployeeId(), StatusCaisse.ACTIVE);       
        CompteCaisse caiss=compteCaisses.get(0);

        Long injection = caiss.getInjection();
        compteCaisseDTO.setInjection(injection + montant);

        CompteCaisse compteCaisse = compteCaisseMapper.toEntity(compteCaisseDTO);
        compteCaisse = compteCaisseRepository.save(compteCaisse);
        getCash();
        return compteCaisseMapper.toDto(compteCaisse);
    }

    @Override
    public CompteCaisseDTO updatePret(CompteCaisseDTO compteCaisseDTO , Long montant) {
        log.debug("Request to update CompteCaisse : {}", compteCaisseDTO);

        List<CompteCaisse> compteCaisses=compteCaisseRepository.findByEmployeeAndCompteActive(TokenManager.getSubject().getEmployeeId(), StatusCaisse.ACTIVE);       
        CompteCaisse caiss=compteCaisses.get(0);

        Long pret = caiss.getPret();
        compteCaisseDTO.setPret(pret + montant);

        CompteCaisse compteCaisse = compteCaisseMapper.toEntity(compteCaisseDTO);
        compteCaisse = compteCaisseRepository.save(compteCaisse);
        // getCash();
        return compteCaisseMapper.toDto(compteCaisse);
    }

    public CompteCaisseDTO getCash(){

        List<CompteCaisse> compteCaisses=compteCaisseRepository.findByEmployeeAndCompteActive(TokenManager.getSubject().getEmployeeId(), StatusCaisse.ACTIVE);       
        CompteCaisse caiss=compteCaisses.get(0);

        Long injection=caiss.getInjection();
        Long sale=caiss.getSale();
        Long pret =caiss.getPret();
        Long cancel =caiss.getCancel();
        Long balance =caiss.getBalance();

        long cash=injection + sale - cancel - pret ;

        caiss.setCash(cash);
        caiss.setAVerser(cash + balance);


        compteCaisseRepository.save(caiss);
        return compteCaisseMapper.toDto(caiss);

    }

    @Override
    public CompteCaisseDTO createCaisseAfterControlByCaissier(CompteCaisseDTO compteCaisseDTO , Long versement){

        CompteCaisse compteCaisse = compteCaisseMapper.toEntity(compteCaisseDTO);
        compteCaisse.setAVerser(versement);
        compteCaisse.setStatus(StatusCaisse.INACTIVE);
        compteCaisse = compteCaisseRepository.save(compteCaisse);        

        CompteCaisse newCompte=new CompteCaisse();

        Long aVerser=compteCaisse.getBalance() + compteCaisse.getInjection() + compteCaisse.getSale() - compteCaisse.getCancel() - compteCaisse.getPret();

        Long balance =aVerser - versement;
        newCompte.setEmployee(compteCaisse.getEmployee());
        newCompte.setAVerser(versement);
        newCompte.setStatus(StatusCaisse.ACTIVE);
        newCompte.setBalance(balance);
        newCompte.setSale((long) 0);
        newCompte.setInjection((long) 0);
        newCompte.setCash((long) 0);
        newCompte.setName("");
        newCompte.setPret((long) 0);
        newCompte.setCancel((long) 0);

        compteCaisseRepository.save(newCompte);

        return compteCaisseMapper.toDto(newCompte);

    }

    @Override
    public Optional<CompteCaisseDTO> partialUpdate(CompteCaisseDTO compteCaisseDTO) {
        log.debug("Request to partially update CompteCaisse : {}", compteCaisseDTO);

        return compteCaisseRepository
            .findById(compteCaisseDTO.getId())
            .map(existingCompteCaisse -> {
                compteCaisseMapper.partialUpdate(existingCompteCaisse, compteCaisseDTO);

                return existingCompteCaisse;
            })
            .map(compteCaisseRepository::save)
            .map(compteCaisseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CompteCaisseDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CompteCaisses");
        return compteCaisseRepository.findAll(pageable).map(compteCaisseMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompteCaisseDTO> findOne(Long id) {
        log.debug("Request to get CompteCaisse : {}", id);
        return compteCaisseRepository.findById(id).map(compteCaisseMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete CompteCaisse : {}", id);
        compteCaisseRepository.deleteById(id);
    }

    @Override
    public List<CompteCaisseDTO> findByEmployeeAndCompteActive(){
        return compteCaisseMapper.toDto(compteCaisseRepository.findByEmployeeAndCompteActive(TokenManager.getSubject().getEmployeeId(), StatusCaisse.ACTIVE));
    }


    // @Override
    // public Optional<CompteCaisse> updateInjection(CompteCaisse caisse){

    //     return compteCaisseRepository.updateInjection(null, null)

    // }

}
