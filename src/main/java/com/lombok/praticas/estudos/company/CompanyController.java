package com.lombok.praticas.estudos.company;

import com.lombok.praticas.estudos.company.Dto.CompanyDto;
import com.lombok.praticas.estudos.company.Dto.CompanySearch;
import com.lombok.praticas.estudos.person.PersonEntity;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/company")
@CrossOrigin
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {this.companyService = companyService;}

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<CompanyDto> create(@RequestBody @Valid CompanyDto companyDto) {
        return ResponseEntity.created(URI.create("/create" + companyDto.id()))
                .body(companyService.companyCreate(companyDto));
    }

    @GetMapping("/list")
    public ResponseEntity<List<CompanyDto>> list() {
        return ResponseEntity.ok(companyService.list());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<CompanyDto>> pagination(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(companyService.pagination(pageable));
    }

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<CompanyDto> update(@PathVariable Long id, @RequestBody @Valid CompanyDto companyDto) {
        return ResponseEntity.ok(companyService.update(id, companyDto));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<CompanyDto> detail(@PathVariable @Valid Long id) {
        Optional<CompanyDto> companyDetailDto = companyService.companyDetail(id);
        return companyDetailDto.map(detailDto -> ResponseEntity.ok()
                        .body(detailDto))
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/search/pagination")
    public ResponseEntity<Page<CompanySearch>> pagedSearch(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(companyService.searchCompanyPagination(name, pageable));
    }

    @GetMapping("/search/list")
    public ResponseEntity<List<CompanySearch>> searchList(@RequestParam String name) {
        return ResponseEntity.ok(companyService.searchListPerson(name));
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<PersonEntity> delete(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent()
                .build();
    }

}
