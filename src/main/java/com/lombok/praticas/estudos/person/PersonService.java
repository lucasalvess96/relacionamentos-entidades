package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.comum.ValidateCpfUser;
import com.lombok.praticas.estudos.person.comum.ValidateUser;
import com.lombok.praticas.estudos.person.dtoo.PersonCreateDto;
import com.lombok.praticas.estudos.person.dtoo.PersonSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record PersonService(PersonRepository personRepository) {

    public PersonCreateDto personCreate(PersonCreateDto personCreateDto) {
        ValidateUser.validateUser(personCreateDto, personRepository);
        ValidateCpfUser.validateCpfUser(personCreateDto, personRepository);
        PersonEntity personEntity = new PersonEntity();
        return getPersonCreateDtoObject(personCreateDto, personEntity);
    }

    public Page<PersonCreateDto> personListPagination(Pageable pageable) {
        return personRepository.findAll(pageable).map(PersonCreateDto::new);
    }

    public List<PersonCreateDto> personList() {
        return personRepository.findAll().stream().map(PersonCreateDto::new).toList();
    }

    public PersonCreateDto personUpdate(Long id, PersonCreateDto personCreateDto) {
        PersonEntity personEntity = personRepository.findById(id).orElseThrow(() -> new ErroRequest("ID não encontrado"));
        return getPersonCreateDtoObject(personCreateDto, personEntity);
    }

    public Optional<PersonCreateDto> detailPerson(Long id) {
        return Optional.ofNullable(personRepository.findById(id).map(PersonCreateDto::new)
                                           .orElseThrow(() -> new ErroRequest("Usuário não encontrado")));
    }

    public Page<PersonSearchDto> searchPersonPagination(String name, Pageable pageable) {
        return personRepository.findByNameContainingIgnoreCase(name, pageable)
                .map(personEntity -> new PersonSearchDto(personEntity.getName()));
    }

    public List<PersonSearchDto> searchListPerson(String name) {
        return personRepository.findByNameContainingIgnoreCase(name).stream()
                .map(personEntity -> new PersonSearchDto(personEntity.getName())).toList();
    }

    public void deletePerson(Long id) {
        personRepository.findById(id).orElseThrow(() -> new ErroRequest("Recurso não encontrado"));
        personRepository.deleteById(id);
    }

    public PersonCreateDto getPersonCreateDtoObject(PersonCreateDto personCreateDto, PersonEntity person) {
        person.setName(personCreateDto.name());
        person.setAge(personCreateDto.age());
        person.setCpf(personCreateDto.cpf());
        return new PersonCreateDto(personRepository.save(person));
    }
}
