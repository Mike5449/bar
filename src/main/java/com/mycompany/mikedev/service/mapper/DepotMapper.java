package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Depot;
import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.service.dto.DepotDTO;
import com.mycompany.mikedev.service.dto.EmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Depot} and its DTO {@link DepotDTO}.
 */
@Mapper(componentModel = "spring")
public interface DepotMapper extends EntityMapper<DepotDTO, Depot> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeFirstName")
    DepotDTO toDto(Depot s);

    @Named("employeeFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    EmployeeDTO toDtoEmployeeFirstName(Employee employee);
}
