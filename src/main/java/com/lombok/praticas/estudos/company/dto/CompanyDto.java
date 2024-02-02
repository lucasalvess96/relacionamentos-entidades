package com.lombok.praticas.estudos.company.dto;

import com.lombok.praticas.estudos.company.Company;
import com.lombok.praticas.estudos.contactperson.ContactPersonDto;

import static com.lombok.praticas.estudos.company.utils.Converter.convertEntityToDto;

public record CompanyDto(Long id, String name, String address, String phone, ContactPersonDto contactPersonDto) {

    public CompanyDto(Company company) {
        this(
                company.getId(), company.getName(), company.getAddress(), company.getPhone(),
                convertEntityToDto(company.getContactPerson())
        );
    }
}
