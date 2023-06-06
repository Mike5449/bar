package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.domain.Salary;
import com.mycompany.mikedev.service.dto.EmployeeDTO;
import com.mycompany.mikedev.service.dto.SalaryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Salary} and its DTO {@link SalaryDTO}.
 */
@Mapper(componentModel = "spring")
public interface SalaryMapper extends EntityMapper<SalaryDTO, Salary> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeFirstName")
    SalaryDTO toDto(Salary s);

    @Named("employeeFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    EmployeeDTO toDtoEmployeeFirstName(Employee employee);
}
