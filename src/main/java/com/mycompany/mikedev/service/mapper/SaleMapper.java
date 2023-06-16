package com.mycompany.mikedev.service.mapper;

import com.mycompany.mikedev.domain.Client;
import com.mycompany.mikedev.domain.Depot;
import com.mycompany.mikedev.domain.Employee;
import com.mycompany.mikedev.domain.Product;
import com.mycompany.mikedev.domain.ProductPrice;
import com.mycompany.mikedev.domain.Sale;
import com.mycompany.mikedev.service.dto.ClientDTO;
import com.mycompany.mikedev.service.dto.DepotDTO;
import com.mycompany.mikedev.service.dto.EmployeeDTO;
import com.mycompany.mikedev.service.dto.ProductDTO;
import com.mycompany.mikedev.service.dto.ProductPriceDTO;
import com.mycompany.mikedev.service.dto.SaleDTO;

import java.util.List;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Sale} and its DTO {@link SaleDTO}.
 */
@Mapper(componentModel = "spring")
public interface SaleMapper extends EntityMapper<SaleDTO, Sale> {
    @Mapping(target = "employee", source = "employee", qualifiedByName = "employeeFirstName")
    @Mapping(target = "client", source = "client", qualifiedByName = "clientName")
    // @Mapping(target = "client.depot", source = "client.depot", qualifiedByName = "depotAmount")
    @Mapping(target = "product", source = "product", qualifiedByName = "productName")
    @Mapping(target = "currentPrice", source = "currentPrice", qualifiedByName = "productPriceId")
    SaleDTO toDto(Sale s);

    @Named("employeeFirstName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    EmployeeDTO toDtoEmployeeFirstName(Employee employee);

    @Named("clientName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ClientDTO toDtoClientName(Client client);

    // @Named("depotAmount")
    // @BeanMapping(ignoreByDefault = true)
    // @Mapping(target = "id", source = "id")
    // @Mapping(target = "amount", source = "amount")
    // DepotDTO toDtoDepotAmount(Depot depot);

    @Named("productName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "price", source = "price")
    ProductDTO toDtoProductName(Product product);

    @Named("productPriceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductPriceDTO toDtoProductPriceId(ProductPrice productPrice);
}
