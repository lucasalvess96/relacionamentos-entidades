package com.lombok.praticas.estudos.persoon;

import com.lombok.praticas.estudos.person.Dto.PersonSearchDto;
import com.lombok.praticas.estudos.person.PersonEntity;
import com.lombok.praticas.estudos.persoon.dto.PersonDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RequestMapping("/persoon")
@CrossOrigin
@Controller
@AllArgsConstructor
public class PersoonController {

    private final PersoonService persoonService;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<PersonDto> create(@RequestBody @Valid PersonDto personCreateDto) {
        PersonDto createDto = persoonService.personCreate(personCreateDto);
        return ResponseEntity.created(URI.create("/create/" + createDto.id()))
                .body(createDto);
    }

    @GetMapping("/list")
    public ResponseEntity<List<PersonDto>> list() {
        return ResponseEntity.ok()
                .body(persoonService.personList());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<PersonDto>> pagination(@PageableDefault(direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok()
                .body(persoonService.personPage(pageable));
    }

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<PersonDto> update(@PathVariable Long id, @RequestBody @Valid PersonDto personDto) {
        PersonDto personUpdate = persoonService.personUpdate(id, personDto);
        return ResponseEntity.ok()
                .body(personUpdate);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PersonDto> detail(@PathVariable @Valid Long id) {
        Optional<PersonDto> personDetailDto = persoonService.personDetail(id);
        return personDetailDto.map(detailDto -> ResponseEntity.ok()
                        .body(detailDto))
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/search/pagination")
    public ResponseEntity<Page<PersonSearchDto>> pagedSearch(@RequestParam String name, Pageable pageable) {
        Page<PersonSearchDto> personSearchDtos = persoonService.searchPersonPagination(name, pageable);
        return ResponseEntity.ok()
                .body(personSearchDtos);
    }

    @GetMapping("/search/list")
    public ResponseEntity<List<PersonSearchDto>> searchList(@RequestParam String name) {
        List<PersonSearchDto> personSearchDtos = persoonService.searchListPerson(name);
        return ResponseEntity.ok()
                .body(personSearchDtos);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<PersonEntity> delete(@PathVariable Long id) {
        persoonService.personDelete(id);
        return ResponseEntity.noContent()
                .build();
    }
}
