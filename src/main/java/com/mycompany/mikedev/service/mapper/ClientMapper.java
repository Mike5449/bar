package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Client;
import com.mycompany.mikedev.service.dto.ClientDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {}
