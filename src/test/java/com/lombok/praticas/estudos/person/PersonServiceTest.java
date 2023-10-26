package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.comun.ErroRequest;
import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;
    
    @Mock
    Pageable pageable;
    
    @InjectMocks
    private PersonService personService;
    
    @Test
    @DisplayName("Should be creation of a person")
    void testPersonCreate() {
        PersonCreateDto personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "123456789");
        
        PersonEntity savedPersonEntity = new PersonEntity();
        savedPersonEntity.setId(1L);
        savedPersonEntity.setName("John Doe");
        savedPersonEntity.setAge("30");
        savedPersonEntity.setCpf("123456789");
        
        when(personRepository.save(any(PersonEntity.class))).thenReturn(savedPersonEntity);
        
        PersonCreateDto result = personService.personCreate(personCreateDto);

        assertAll(() -> {
            assertEquals(personCreateDto.id(), result.id());
            assertEquals(personCreateDto.name(), result.name());
            assertEquals(personCreateDto.age(), result.age());
            assertEquals(personCreateDto.cpf(), result.cpf());
        });
    }

    @Test
    @DisplayName("Should test person list pagination")
    void testPersonListPagination() {
        List<PersonEntity> personEntities = Collections.singletonList(new PersonEntity(1L, "John Doe", "30", "123456789"));
        
        Page<PersonEntity> personEntityPage = new PageImpl<>(personEntities, pageable, 1);
        
        when(personRepository.findAll(pageable)).thenReturn(personEntityPage);
        
        Page<PersonCreateDto> result = personService.personListPagination(pageable);
        
        assertEquals(1, result.getContent().size());
        assertEquals("John Doe", result.getContent().get(0).name());
    }

    @Test
    @DisplayName("Should test person list pagination when the list is empty")
    void testPersonListPaginationEmpty() {
        List<PersonEntity> emptyList = Collections.emptyList();
        Page<PersonEntity> emptyPage = new PageImpl<>(emptyList, pageable, 0);

        when(personRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<PersonCreateDto> result = personService.personListPagination(pageable);

        assertEquals(0, result.getContent().size());
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return list of PersonCreateDto")
    void testPersonList() {
        List<PersonEntity> personEntities = Arrays.asList(
                new PersonEntity(1L, "John Doe", "30", "123456789"),
                new PersonEntity(2L, "Jane Smith", "25", "987654321")
        );

        when(personRepository.findAll()).thenReturn(personEntities);

        List<PersonCreateDto> result = personService.personList();

        assertEquals(2, result.size());

        PersonCreateDto firstPerson = result.get(0);
        assertAll(
                () -> assertEquals(1L, firstPerson.id()),
                () -> assertEquals("John Doe", firstPerson.name()),
                () -> assertEquals("30", firstPerson.age()),
                () -> assertEquals("123456789", firstPerson.cpf())
        );

        PersonCreateDto secondPerson = result.get(1);
        assertAll(
                () -> assertEquals(2L, secondPerson.id()),
                () -> assertEquals("Jane Smith", secondPerson.name()),
                () -> assertEquals("25", secondPerson.age()),
                () -> assertEquals("987654321", secondPerson.cpf())
        );
    }

    @Test
    @DisplayName("Should test person list with empty result")
    void testPersonListEmpty() {
        when(personRepository.findAll()).thenReturn(Collections.emptyList());

        List<PersonCreateDto> result = personService.personList();

        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should update a person")
    void testPersonUpdate() {
        Long id = 1L;
        PersonCreateDto personCreateDto = new PersonCreateDto(id, "John Doe", "30", "12345678912");

        PersonEntity personEntity = new PersonEntity();
        personEntity.setId(id);
        personEntity.setName("John Doe atualizado");
        personEntity.setAge("34");
        personEntity.setCpf("02345678977");

        when(personRepository.findById(id)).thenReturn(Optional.of(personEntity));
        when(personRepository.save(any(PersonEntity.class))).thenReturn(personEntity);

        PersonCreateDto updatedPerson = personService.personUpdate(id, personCreateDto);

        assertAll(() -> {
            assertEquals(personCreateDto.id(), updatedPerson.id());
            assertEquals(personCreateDto.name(), updatedPerson.name());
            assertEquals(personCreateDto.age(), updatedPerson.age());
            assertEquals(personCreateDto.cpf(), updatedPerson.cpf());
        });
    }

    @Test
    @DisplayName("Should throw an exception when the ID is not found")
    void testPersonUpdateNotFound() {
        Long id = 1L;
        PersonCreateDto personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "12345678912");

        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ErroRequest.class, () -> personService.personUpdate(id, personCreateDto));
    }
    
    @Test
    @DisplayName("Should return PersonCreateDto object for a valid ID")
    void testDetailPersonWithValidId() {
        Long id = 1L;
        PersonEntity personEntity = new PersonEntity(1L, "John Doe", "30", "123456789");
        Optional<PersonEntity> optionalPersonEntity = Optional.of(personEntity);

        when(personRepository.findById(id)).thenReturn(optionalPersonEntity);

        Optional<PersonCreateDto> result = personService.detailPerson(id);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().name());
        assertEquals("30", result.get().age());
        assertEquals("123456789", result.get().cpf());
    }

    @Test
    @DisplayName("Should throw ErroRequest for an invalid ID")
    void testDetailPersonWithInvalidId() {
        Long id = 2L;
        when(personRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(ErroRequest.class, () -> personService.detailPerson(id));
        assertEquals("Usuário não encontrado", exception.getMessage());
    }
}
