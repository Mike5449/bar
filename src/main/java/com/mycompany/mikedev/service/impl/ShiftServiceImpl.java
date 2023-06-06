package com.mycompany.mikedev.service.impl;

import com.mycompany.mikedev.domain.Shift;
import com.mycompany.mikedev.repository.ShiftRepository;
import com.mycompany.mikedev.service.ShiftService;
import com.mycompany.mikedev.service.dto.ShiftDTO;
import com.mycompany.mikedev.service.mapper.ShiftMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Shift}.
 */
@Service
@Transactional
public class ShiftServiceImpl implements ShiftService {

    private final Logger log = LoggerFactory.getLogger(ShiftServiceImpl.class);

    private final ShiftRepository shiftRepository;

    private final ShiftMapper shiftMapper;

    public ShiftServiceImpl(ShiftRepository shiftRepository, ShiftMapper shiftMapper) {
        this.shiftRepository = shiftRepository;
        this.shiftMapper = shiftMapper;
    }

    @Override
    public ShiftDTO save(ShiftDTO shiftDTO) {
        log.debug("Request to save Shift : {}", shiftDTO);
        Shift shift = shiftMapper.toEntity(shiftDTO);
        shift = shiftRepository.save(shift);
        return shiftMapper.toDto(shift);
    }

    @Override
    public ShiftDTO update(ShiftDTO shiftDTO) {
        log.debug("Request to update Shift : {}", shiftDTO);
        Shift shift = shiftMapper.toEntity(shiftDTO);
        shift = shiftRepository.save(shift);
        return shiftMapper.toDto(shift);
    }

    @Override
    public Optional<ShiftDTO> partialUpdate(ShiftDTO shiftDTO) {
        log.debug("Request to partially update Shift : {}", shiftDTO);

        return shiftRepository
            .findById(shiftDTO.getId())
            .map(existingShift -> {
                shiftMapper.partialUpdate(existingShift, shiftDTO);

                return existingShift;
            })
            .map(shiftRepository::save)
            .map(shiftMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ShiftDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Shifts");
        return shiftRepository.findAll(pageable).map(shiftMapper::toDto);
    }

    public Page<ShiftDTO> findAllWithEagerRelationships(Pageable pageable) {
        return shiftRepository.findAllWithEagerRelationships(pageable).map(shiftMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ShiftDTO> findOne(Long id) {
        log.debug("Request to get Shift : {}", id);
        return shiftRepository.findOneWithEagerRelationships(id).map(shiftMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Shift : {}", id);
        shiftRepository.deleteById(id);
    }
}
