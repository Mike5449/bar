package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Attendance;
import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.domain.Job;
import com.mycompany.mikedev.service.dto.AttendanceDTO;
import com.mycompany.mikedev.service.dto.EmployeeDTO;
import com.mycompany.mikedev.service.dto.JobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Employee} and its DTO {@link EmployeeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmployeeMapper extends EntityMapper<EmployeeDTO, Employee> {
    @Mapping(target = "job", source = "job", qualifiedByName = "jobJobName")   
    // @Mapping(target = "attendance", source = "attendance", qualifiedByName = "date")

    EmployeeDTO toDto(Employee s);

    @Named("jobJobName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "jobName", source = "jobName")
    JobDTO toDtoJobJobName(Job job);

    // @Named("date")
    // @BeanMapping(ignoreByDefault = true)
    // @Mapping(target = "id", source = "id")
    // @Mapping(target = "date", source = "date")
    // AttendanceDTO toDtoDate(Attendance attendance);

}
