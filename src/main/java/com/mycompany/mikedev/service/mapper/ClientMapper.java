package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Client;
import com.mycompany.mikedev.domain.Depot;
import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.service.dto.ClientDTO;
import com.mycompany.mikedev.service.dto.DepotDTO;
import com.mycompany.mikedev.service.dto.EmployeeDTO;

import java.util.List;
import java.util.stream.Collectors;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Client} and its DTO {@link ClientDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClientMapper extends EntityMapper<ClientDTO, Client> {

//     @Mapping(target = "depot", source = "depot", qualifiedByName = "depotAmount")
//     ClientDTO toDto(Client s);

//     @Named("depotAmount")
//     @BeanMapping(ignoreByDefault = true)
//     @Mapping(target = "id", source = "id")
//     @Mapping(target = "amount", source = "amount")
//    List<DepotDTO>toDtoDepotAmount(List<Depot> depot);

    // default List<DepotDTO> mapDepotsToDTOs(List<Depot> depots){
    //     return depots.stream()
    //     .map(this::mapDepotToDTO)
    //     .collect(Collectors.toList());
    // }

    // default DepotDTO mapDepotToDTO(Depot depot){
    //     DepotDTO depotDTO=new DepotDTO();
    //     return depotDTO;
    // }

//     @Named("employeId")
//     @BeanMapping(ignoreByDefault = true)
//     @Mapping(target = "id", source = "id")
//    EmployeeDTO toDtoDepotAmount(Employee employee);
}
