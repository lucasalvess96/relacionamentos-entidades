package com.lombok.praticas.estudos.embeddable.company.dto;

import com.lombok.praticas.estudos.embeddable.company.Company;
import com.lombok.praticas.estudos.embeddable.contactperson.ContactPersonDto;

import static com.lombok.praticas.estudos.embeddable.company.utils.Converter.convertEntityToDto;

public record CompanyDto(Long id, String name, String address, String phone, ContactPersonDto contactPersonDto) {

    public CompanyDto(Company company) {
        this(
                company.getId(),
                company.getName(),
                company.getAddress(),
                company.getPhone(),
                convertEntityToDto(company.getContactPerson())
        );
    }
}
