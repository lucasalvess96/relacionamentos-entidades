package com.lombok.praticas.estudos.embeddable.company;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.embeddable.company.dto.CompanyDto;
import com.lombok.praticas.estudos.embeddable.company.dto.CompanySearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.lombok.praticas.estudos.embeddable.company.utils.Converter.convertDtoToEntity;

@Service
public record CompanyService(CompanyRepository companyRepository) {

    public CompanyDto companyCreate(CompanyDto companyDto) {
        Company company = new Company();
        return getCompanyCreateDtoAndUpdateDto(company, companyDto);
    }

    public List<CompanyDto> list() {
        List<Company> companyList = companyRepository.findAll();
        return companyList.stream().map(CompanyDto::new).toList();
    }

    public Page<CompanyDto> pagination(Pageable pageable) {
        Page<Company> companyPage = companyRepository.findAll(pageable);
        return companyPage.map(CompanyDto::new);
    }

    public CompanyDto update(Long id, CompanyDto companyDto) {
        Company company = companyRepository.findById(id).orElseThrow(() -> new ErroRequest("informação não encontrada"));
        return getCompanyCreateDtoAndUpdateDto(company, companyDto);
    }

    public Optional<CompanyDto> companyDetail(Long id) {
        Optional<Company> company = companyRepository.findById(id);
        return company.map(entity -> Optional.of(new CompanyDto(entity)))
                .orElseThrow(() -> new ErroRequest("informação não encontrada"));
    }

    public Page<CompanySearch> searchCompanyPagination(String name, Pageable pageable) {
        Page<Company> companyEntityPage = companyRepository.findByNameContainingIgnoreCase(name, pageable);
        return companyEntityPage.map(entity -> new CompanySearch(entity.getName()));
    }

    public List<CompanySearch> searchListPerson(String name) {
        List<Company> companyEntities = companyRepository.findByNameContainingIgnoreCase(name);
        return companyEntities.stream().map(entity -> new CompanySearch(entity.getName())).toList();
    }

    public void deleteCompany(Long id) {
        companyRepository.findById(id).orElseThrow(() -> new ErroRequest("Recurso não encontrado"));
        companyRepository.deleteById(id);
    }

    private CompanyDto getCompanyCreateDtoAndUpdateDto(Company company, CompanyDto companyDto) {
        company.setName(companyDto.name());
        company.setAddress(companyDto.address());
        company.setPhone(companyDto.phone());
        company.setContactPerson(convertDtoToEntity(companyDto.contactPersonDto()));
        companyRepository.save(company);
        return new CompanyDto(company);
    }
}
