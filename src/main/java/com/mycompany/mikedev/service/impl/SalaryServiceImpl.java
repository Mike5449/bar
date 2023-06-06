package com.mycompany.mikedev.service.impl;

import com.mycompany.mikedev.domain.Salary;
import com.mycompany.mikedev.repository.SalaryRepository;
import com.mycompany.mikedev.service.SalaryService;
import com.mycompany.mikedev.service.dto.SalaryDTO;
import com.mycompany.mikedev.service.mapper.SalaryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Salary}.
 */
@Service
@Transactional
public class SalaryServiceImpl implements SalaryService {

    private final Logger log = LoggerFactory.getLogger(SalaryServiceImpl.class);

    private final SalaryRepository salaryRepository;

    private final SalaryMapper salaryMapper;

    public SalaryServiceImpl(SalaryRepository salaryRepository, SalaryMapper salaryMapper) {
        this.salaryRepository = salaryRepository;
        this.salaryMapper = salaryMapper;
    }

    @Override
    public SalaryDTO save(SalaryDTO salaryDTO) {
        log.debug("Request to save Salary : {}", salaryDTO);
        Salary salary = salaryMapper.toEntity(salaryDTO);
        salary = salaryRepository.save(salary);
        return salaryMapper.toDto(salary);
    }

    @Override
    public SalaryDTO update(SalaryDTO salaryDTO) {
        log.debug("Request to update Salary : {}", salaryDTO);
        Salary salary = salaryMapper.toEntity(salaryDTO);
        salary = salaryRepository.save(salary);
        return salaryMapper.toDto(salary);
    }

    @Override
    public Optional<SalaryDTO> partialUpdate(SalaryDTO salaryDTO) {
        log.debug("Request to partially update Salary : {}", salaryDTO);

        return salaryRepository
            .findById(salaryDTO.getId())
            .map(existingSalary -> {
                salaryMapper.partialUpdate(existingSalary, salaryDTO);

                return existingSalary;
            })
            .map(salaryRepository::save)
            .map(salaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SalaryDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Salaries");
        return salaryRepository.findAll(pageable).map(salaryMapper::toDto);
    }

    public Page<SalaryDTO> findAllWithEagerRelationships(Pageable pageable) {
        return salaryRepository.findAllWithEagerRelationships(pageable).map(salaryMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SalaryDTO> findOne(Long id) {
        log.debug("Request to get Salary : {}", id);
        return salaryRepository.findOneWithEagerRelationships(id).map(salaryMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Salary : {}", id);
        salaryRepository.deleteById(id);
    }
}
