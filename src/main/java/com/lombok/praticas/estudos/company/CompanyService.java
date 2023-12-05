package com.lombok.praticas.estudos.company;

import com.lombok.praticas.estudos.contactPerson.ContactPersonEntity;
import org.springframework.stereotype.Service;

import static com.lombok.praticas.estudos.company.Utils.Converter.convertDtoToEntity;

@Service
public record CompanyService(CompanyRepository companyRepository) {

    public CompanyDto companyCreate(CompanyDto companyDto) {
        Company company = new Company();
        return getCompanyCreateDtoAndUpdateDto(company, companyDto);
    }

    private CompanyDto getCompanyCreateDtoAndUpdateDto(Company company, CompanyDto companyDto) {
        company.setName(companyDto.name());
        company.setAddress(companyDto.address());
        company.setPhone(companyDto.phone());
        ContactPersonEntity contactPersonEntity = convertDtoToEntity(companyDto.contactPersonDto());
        company.setContactPerson(contactPersonEntity);
        companyRepository.save(company);
        return new CompanyDto(company);
    }
}
