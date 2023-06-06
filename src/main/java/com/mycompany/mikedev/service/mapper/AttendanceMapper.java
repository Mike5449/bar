package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Attendance;
import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.service.dto.AttendanceDTO;
import com.mycompany.mikedev.service.dto.EmployeeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Attendance} and its DTO {@link AttendanceDTO}.
 */
@Mapper(componentModel = "spring")
public interface AttendanceMapper extends EntityMapper<AttendanceDTO, Attendance> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeFirstName")
    AttendanceDTO toDto(Attendance s);

    @Named("employeeFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    EmployeeDTO toDtoEmployeeFirstName(Employee employee);
}
