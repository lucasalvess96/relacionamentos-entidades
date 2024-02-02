package com.lombok.praticas.estudos.persoon;

import com.lombok.praticas.estudos.address.AddressEntity;
import com.lombok.praticas.estudos.address.AddressRepository;
import com.lombok.praticas.estudos.address.dto.AddressDto;
import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.Dto.PersonSearchDto;
import com.lombok.praticas.estudos.persoon.dto.PersonDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public record PersoonService(PersoonRepository persoonRepository, AddressRepository addressRepository) {

    public PersonDto personCreate(PersonDto dto) {
        Person person = new Person();
        return getPersonCreateDtoAndUpdateDto(dto, person);
    }

    public List<PersonDto> personList() {
        List<Person> person = persoonRepository.findAll();
        return person.stream().map(PersonDto::new).toList();
    }

    public Page<PersonDto> personPage(Pageable pageable) {
        Page<Person> person = persoonRepository.findAll(pageable);
        return person.map(PersonDto::new);
    }

    public PersonDto personUpdate(Long id, PersonDto personDto) {
        Person person = persoonRepository.findById(id)
                .orElseThrow(() -> new ErroRequest("informação não encontrada"));
        return getPersonCreateDtoAndUpdateDto(personDto, person);
    }

    public Optional<PersonDto> personDetail(Long id) {
        Optional<Person> personEntity = persoonRepository.findById(id);
        return personEntity.map(entity -> Optional.of(new PersonDto(entity)))
                .orElseThrow(() -> new ErroRequest("Usuário não encontrado"));
    }

    public Page<PersonSearchDto> searchPersonPagination(String name, Pageable pageable) {
        Page<Person> personEntityPage = persoonRepository.findByNameContainingIgnoreCase(name, pageable);
        return personEntityPage.map(personEntity -> new PersonSearchDto(personEntity.getName()));
    }

    public List<PersonSearchDto> searchListPerson(String name) {
        List<Person> personEntities = persoonRepository.findByNameContainingIgnoreCase(name);
        return personEntities.stream().map(personEntity -> new PersonSearchDto(personEntity.getName())).toList();
    }

    public void personDelete(Long id) {
        if (persoonRepository.existsById(id)) {
            persoonRepository.deleteById(id);
        } else {
            throw new ErroRequest("Recurso não encontrado");
        }
    }

    private PersonDto getPersonCreateDtoAndUpdateDto(PersonDto personCreateDto, Person personEntity) {
        personEntity.setName(personCreateDto.name());
        personEntity.setCpf(personCreateDto.cpf());
        AddressDto addressDto = personCreateDto.addressDto();
        AddressEntity addressEntity = personEntity.getAddress();
        if (addressEntity == null) {
            addressEntity = new AddressEntity();
            personEntity.setAddress(addressEntity);
        }
        if (addressDto != null) {
            addressEntity.setId(addressDto.id());
            addressEntity.setStreet(addressDto.street());
            addressEntity.setNumber(addressDto.number());
            addressEntity.setCity(addressDto.city());
        }
        personEntity.setAddress(addressRepository.save(addressEntity));
        return new PersonDto(persoonRepository.save(personEntity));
    }
}
