package com.mycompany.mikedev.service.impl;

import com.mycompany.mikedev.domain.Depot;
import com.mycompany.mikedev.repository.DepotRepository;
import com.mycompany.mikedev.service.DepotService;
import com.mycompany.mikedev.service.dto.DepotDTO;
import com.mycompany.mikedev.service.mapper.DepotMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Depot}.
 */
@Service
@Transactional
public class DepotServiceImpl implements DepotService {

    private final Logger log = LoggerFactory.getLogger(DepotServiceImpl.class);

    private final DepotRepository depotRepository;

    private final DepotMapper depotMapper;

    public DepotServiceImpl(DepotRepository depotRepository, DepotMapper depotMapper) {
        this.depotRepository = depotRepository;
        this.depotMapper = depotMapper;
    }

    @Override
    public DepotDTO save(DepotDTO depotDTO) {
        log.debug("Request to save Depot : {}", depotDTO);
        Depot depot = depotMapper.toEntity(depotDTO);
        depot = depotRepository.save(depot);
        return depotMapper.toDto(depot);
    }

    @Override
    public DepotDTO update(DepotDTO depotDTO) {
        log.debug("Request to update Depot : {}", depotDTO);
        Depot depot = depotMapper.toEntity(depotDTO);
        depot = depotRepository.save(depot);
        return depotMapper.toDto(depot);
    }

    @Override
    public Optional<DepotDTO> partialUpdate(DepotDTO depotDTO) {
        log.debug("Request to partially update Depot : {}", depotDTO);

        return depotRepository
            .findById(depotDTO.getId())
            .map(existingDepot -> {
                depotMapper.partialUpdate(existingDepot, depotDTO);

                return existingDepot;
            })
            .map(depotRepository::save)
            .map(depotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<DepotDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Depots");
        return depotRepository.findAll(pageable).map(depotMapper::toDto);
    }

    public Page<DepotDTO> findAllWithEagerRelationships(Pageable pageable) {
        return depotRepository.findAllWithEagerRelationships(pageable).map(depotMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DepotDTO> findOne(Long id) {
        log.debug("Request to get Depot : {}", id);
        return depotRepository.findOneWithEagerRelationships(id).map(depotMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Depot : {}", id);
        depotRepository.deleteById(id);
    }
}
