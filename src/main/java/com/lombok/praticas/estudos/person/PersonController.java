package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import com.lombok.praticas.estudos.person.Dto.PersonSearchDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("person")
@CrossOrigin
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<PersonCreateDto> create(@RequestBody @Valid PersonCreateDto personCreateDto) {
        PersonCreateDto createDto = personService.personCreate(personCreateDto);
        return ResponseEntity.created(URI.create("/create/" + createDto.id()))
                .body(createDto);
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<PersonCreateDto>> list(@PageableDefault(direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(personService.personListPagination(pageable));
    }

    @GetMapping("/list")
    public ResponseEntity<List<PersonCreateDto>> list() {
        return ResponseEntity.ok(personService.personList());
    }

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<PersonCreateDto> update(@PathVariable Long id,
                                                  @RequestBody @Valid PersonCreateDto personCreateDto) {
        return ResponseEntity.ok(personService.personUpdate(id, personCreateDto));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PersonCreateDto> detail(@PathVariable @Valid Long id) {
        Optional<PersonCreateDto> personDetailDto = personService.detailPerson(id);
        return personDetailDto.map(detailDto -> ResponseEntity.ok()
                        .body(detailDto))
                .orElseGet(() -> ResponseEntity.notFound()
                        .build());
    }

    @GetMapping("/search/pagination")
    public ResponseEntity<Page<PersonSearchDto>> pagedSearch(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(personService.searchPersonPagination(name, pageable));
    }

    @GetMapping("/search/list")
    public ResponseEntity<List<PersonSearchDto>> searchList(@RequestParam String name) {
        return ResponseEntity.ok(personService.searchListPerson(name));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PersonEntity> delete(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent()
                .build();
    }
}
