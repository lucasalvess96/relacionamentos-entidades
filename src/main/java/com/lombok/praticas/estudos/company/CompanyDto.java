package com.lombok.praticas.estudos.company;

import com.lombok.praticas.estudos.contactPerson.ContactPersonDto;

import static com.lombok.praticas.estudos.company.Utils.Converter.convertEntityToDto;

public record CompanyDto(Long id, String name, String address, String phone, ContactPersonDto contactPersonDto) {

    public CompanyDto(Company company) {
        this(
                company.getId(), company.getName(), company.getAddress(), company.getPhone(),
                convertEntityToDto(company.getContactPerson())
        );
    }
}
