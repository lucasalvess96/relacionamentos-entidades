package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.person.dtoo.PersonCreateDto;
import com.lombok.praticas.estudos.person.dtoo.PersonSearchDto;
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
public class PersonController implements Personswagger {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping("/create")
    @Transactional
    @Override
    public ResponseEntity<PersonCreateDto> create(@RequestBody @Valid PersonCreateDto personCreateDto) {
        PersonCreateDto createDto = personService.personCreate(personCreateDto);
        return ResponseEntity.created(URI.create("/person/create/" + createDto.id())).body(createDto);
    }

    @GetMapping("/pagination")
    @Override
    public ResponseEntity<Page<PersonCreateDto>> list(@PageableDefault(direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(personService.personListPagination(pageable));
    }

    @GetMapping("/list")
    @Override
    public ResponseEntity<List<PersonCreateDto>> listing() {
        return ResponseEntity.ok(personService.personList());
    }

    @PutMapping("/update/{id}")
    @Transactional
    @Override
    public ResponseEntity<PersonCreateDto> update(@PathVariable Long id,
                                                  @RequestBody @Valid PersonCreateDto personCreateDto) {
        return ResponseEntity.ok(personService.personUpdate(id, personCreateDto));
    }

    @GetMapping("/detail/{id}")
    @Override
    public ResponseEntity<PersonCreateDto> detail(@PathVariable @Valid Long id) {
        Optional<PersonCreateDto> personDetailDto = personService.detailPerson(id);
        return personDetailDto.map(detailDto -> ResponseEntity.ok().body(detailDto))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search/pagination")
    @Override
    public ResponseEntity<Page<PersonSearchDto>> pagedSearch(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(personService.searchPersonPagination(name, pageable));
    }

    @GetMapping("/search/list")
    @Override
    public ResponseEntity<List<PersonSearchDto>> searchList(@RequestParam String name) {
        return ResponseEntity.ok(personService.searchListPerson(name));
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public ResponseEntity<PersonEntity> delete(@PathVariable Long id) {
        personService.deletePerson(id);
        return ResponseEntity.noContent().build();
    }
}
