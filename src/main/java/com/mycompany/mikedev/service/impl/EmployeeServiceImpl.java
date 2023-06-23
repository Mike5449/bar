package com.mycompany.mikedev.service.impl;

import com.mycompany.mikedev.domain.CompteCaisse;
import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.domain.enumeration.StatusCaisse;
import com.mycompany.mikedev.repository.CompteCaisseRepository;
import com.mycompany.mikedev.repository.EmployeeRepository;
import com.mycompany.mikedev.service.EmployeeService;
import com.mycompany.mikedev.service.UserService;
import com.mycompany.mikedev.service.dto.AdminUserDTO;
import com.mycompany.mikedev.service.dto.EmployeeDTO;
import com.mycompany.mikedev.service.mapper.EmployeeMapper;
import com.mycompany.mikedev.util.DateUtil;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing {@link Employee}.
 */
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    private final EmployeeRepository employeeRepository;

    private final CompteCaisseRepository compteCaisseRepository;

    private final EmployeeMapper employeeMapper;

    @Autowired
    private UserService userService;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmployeeMapper employeeMapper , CompteCaisseRepository compteCaisseRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeMapper = employeeMapper;
        this.compteCaisseRepository = compteCaisseRepository;
    }

    @Override
    public EmployeeDTO save(EmployeeDTO employeeDTO) {
        log.debug("Request to save Employee : {}", employeeDTO);
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee = employeeRepository.save(employee);

        if(employee != null){
            AdminUserDTO adminUserDTO=new AdminUserDTO();
            adminUserDTO.setLogin(employeeDTO.getLogin());
            adminUserDTO.setFirstName(employeeDTO.getFirstName());
            adminUserDTO.setLastName(employeeDTO.getLastName());
            adminUserDTO.setEmail(employeeDTO.getEmail() != null ? employeeDTO.getEmail() : employeeDTO.getFirstName().concat("@gmail.com"));
            adminUserDTO.setActivated(true);
            userService.createCoustumUser(adminUserDTO, employeeDTO.getPassword(), employee);

            if(employee.getJob().equals("Caissier(e)")){

                CompteCaisse compteCaisse = new CompteCaisse();
                compteCaisse.setEmployee(employee);
                compteCaisse.setSale((long) 0);
                compteCaisse.setAVerser((long) 0);
                compteCaisse.setBalance((long) 0);
                compteCaisse.setInjection((long) 0);
                compteCaisse.setPret((long) 0);
                compteCaisse.setCash((long) 0);
                compteCaisse.setCancel((long) 0);
                compteCaisse.setName("");
                compteCaisse.setStatus(StatusCaisse.ACTIVE);
                compteCaisseRepository.save(compteCaisse);

            }

            
        }
        return employeeMapper.toDto(employee);
    }

    @Override
    public EmployeeDTO update(EmployeeDTO employeeDTO) {
        log.debug("Request to update Employee : {}", employeeDTO);
        Employee employee = employeeMapper.toEntity(employeeDTO);
        employee = employeeRepository.save(employee);
        return employeeMapper.toDto(employee);
    }

    @Override
    public Optional<EmployeeDTO> partialUpdate(EmployeeDTO employeeDTO) {
        log.debug("Request to partially update Employee : {}", employeeDTO);

        return employeeRepository
            .findById(employeeDTO.getId())
            .map(existingEmployee -> {
                employeeMapper.partialUpdate(existingEmployee, employeeDTO);

                return existingEmployee;
            })
            .map(employeeRepository::save)
            .map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Employees");
        return employeeRepository.findAll(pageable).map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly=true)
    public List<Employee> findPresentEmployee(){
        return employeeRepository.findPresentEmployee(DateUtil.getDateInString(DateUtil.getCurrentDate(), "-") , "Serveur(se)");
    }

    public Page<EmployeeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return employeeRepository.findAllWithEagerRelationships(pageable).map(employeeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<EmployeeDTO> findOne(Long id) {
        log.debug("Request to get Employee : {}", id);
        return employeeRepository.findOneWithEagerRelationships(id).map(employeeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Employee : {}", id);
        employeeRepository.deleteById(id);
    }
}