package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.domain.LeaveRequest;
import com.mycompany.mikedev.service.dto.EmployeeDTO;
import com.mycompany.mikedev.service.dto.LeaveRequestDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeaveRequest} and its DTO {@link LeaveRequestDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeaveRequestMapper extends EntityMapper<LeaveRequestDTO, LeaveRequest> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeFirstName")
    LeaveRequestDTO toDto(LeaveRequest s);

    @Named("employeeFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    EmployeeDTO toDtoEmployeeFirstName(Employee employee);
}
