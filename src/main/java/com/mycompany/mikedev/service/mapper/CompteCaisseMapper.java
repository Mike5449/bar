package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.CompteCaisse;
import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.service.dto.CompteCaisseDTO;
import com.mycompany.mikedev.service.dto.EmployeeDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompteCaisse} and its DTO {@link CompteCaisseDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompteCaisseMapper extends EntityMapper<CompteCaisseDTO, CompteCaisse> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeFirstName")

    CompteCaisseDTO toDto(CompteCaisse s);

    @Named("employeeFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName") 
    EmployeeDTO toDtoEmployeeFirstName(Employee employee);
}
