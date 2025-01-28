package com.lombok.praticas.estudos.onetoone.persoon;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.onetoone.address.AddressService;
import com.lombok.praticas.estudos.onetoone.address.dto.AddressDto;
import com.lombok.praticas.estudos.onetoone.persoon.dto.PersonDto;
import com.lombok.praticas.estudos.person.dtoo.PersonSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersoonService {

    private final PersoonRepository persoonRepository;

    private final AddressService addressService;

    public PersoonService(PersoonRepository personRepository, AddressService addressService) {
        this.persoonRepository = personRepository;
        this.addressService = addressService;
    }

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

        personEntity.setAddress(addressService.saveAddress(addressDto));
        return new PersonDto(persoonRepository.save(personEntity));
    }
}
