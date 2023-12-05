package com.lombok.praticas.estudos.company;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/company")
@CrossOrigin
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {this.companyService = companyService;}

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<CompanyDto> create(@RequestBody @Valid CompanyDto companyDto) {
        CompanyDto companyCreate = companyService.companyCreate(companyDto);
        return ResponseEntity.created(URI.create("/create" + companyDto.id()))
                .body(companyCreate);
    }
}
