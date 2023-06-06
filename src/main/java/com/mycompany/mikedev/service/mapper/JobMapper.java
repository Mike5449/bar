package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Job;
import com.mycompany.mikedev.service.dto.JobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Job} and its DTO {@link JobDTO}.
 */
@Mapper(componentModel = "spring")
public interface JobMapper extends EntityMapper<JobDTO, Job> {}
