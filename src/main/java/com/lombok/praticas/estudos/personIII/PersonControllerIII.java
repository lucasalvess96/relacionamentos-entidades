package com.lombok.praticas.estudos.personIII;

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

@RestController
@RequestMapping("person-v3")
@CrossOrigin
public class PersonControllerIII {

    private final Personserviceiii personserviceiii;

    public PersonControllerIII(Personserviceiii personserviceiii) {
        this.personserviceiii = personserviceiii;
    }

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<PersonDtoIII> create(@RequestBody @Valid PersonDtoIII person) {
        return ResponseEntity.created(URI.create("/person-v3/create")).body(personserviceiii.create(person));
    }

    @GetMapping("/list")
    public ResponseEntity<List<PersonDtoIII>> list() {
        return ResponseEntity.ok(personserviceiii.list());
    }

    @GetMapping("/pagination")
    public ResponseEntity<Page<PersonDtoIII>> pagination(@PageableDefault(direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(personserviceiii.pagination(pageable));
    }

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<PersonDtoIII> update(@PathVariable Long id, @RequestBody @Valid PersonDtoIII person) {
        return ResponseEntity.ok(personserviceiii.update(id, person));
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<PersonDtoIII> detail(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(personserviceiii.detail(id));
    }

    @GetMapping("/search/list")
    public ResponseEntity<List<PersonSearchDto>> searchList(@RequestParam String name) {
        return ResponseEntity.ok(personserviceiii.searchList(name));
    }

    @GetMapping("/search/pagination")
    public ResponseEntity<Page<PersonSearchDto>> paginationSearch(@RequestParam String name, Pageable pageable) {
        return ResponseEntity.ok(personserviceiii.searchPagination(name, pageable));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<PersonEntityIII> delete(@PathVariable Long id) {
        personserviceiii.delete(id);
        return ResponseEntity.noContent().build();
    }
}
