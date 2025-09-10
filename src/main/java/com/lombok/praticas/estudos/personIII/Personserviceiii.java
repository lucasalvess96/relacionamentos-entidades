package com.lombok.praticas.estudos.personIII;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.dtoo.PersonSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Personserviceiii {

    private final PersonRepositoryIII personRepositoryIII;

    public Personserviceiii(PersonRepositoryIII personRepositoryIII) {
        this.personRepositoryIII = personRepositoryIII;
    }

    public PersonDtoIII create(PersonDtoIII person) {
        PersonEntityIII savedEntity = personRepositoryIII.save(PersonConverter.toEntity(person));
        return PersonConverter.toDto(savedEntity);
    }

    public List<PersonDtoIII> list() {
        return personRepositoryIII.findAll().stream()
                .map(PersonConverter::toDto)
                .toList();
    }

    public Page<PersonDtoIII> pagination(Pageable pageable) {
        return personRepositoryIII.findAll(pageable).map(PersonConverter::toDto);
    }

    public PersonDtoIII update(Long id, PersonDtoIII personDto) {
        PersonEntityIII existingPerson = personRepositoryIII.findById(id).orElseThrow(() -> new ErroRequest("Person not found"));
        existingPerson.setName(personDto.name());
        existingPerson.setAge(personDto.age());
        existingPerson.setCpf(personDto.cpf());
        return PersonConverter.toDto(personRepositoryIII.save(existingPerson));
    }

    public PersonDtoIII detail(Long id) {
        PersonEntityIII person = personRepositoryIII.findById(id).orElseThrow(() -> new ErroRequest("Person not found"));
        return PersonConverter.toDto(person);
    }

    public List<PersonSearchDto> searchList(String name) {
        return personRepositoryIII.findByNameContainingIgnoreCase(name).stream()
                .map(personEntity -> new PersonSearchDto(personEntity.getName())).toList();
    }

    public Page<PersonSearchDto> searchPagination(String name, Pageable pageable) {
        return personRepositoryIII.findByNameContainingIgnoreCase(name, pageable)
                .map(personEntity -> new PersonSearchDto(personEntity.getName()));
    }

    public void delete(Long id) {
        if (!personRepositoryIII.existsById(id)) throw new ErroRequest("Person not found");
        personRepositoryIII.deleteById(id);
    }
}
