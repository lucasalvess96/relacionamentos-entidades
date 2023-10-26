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
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {
    
    @Mock
    private PersonService personService;
    
    @Mock
    Pageable pageable;
    
    @InjectMocks
    private PersonController personController;
    
    @Test
    @DisplayName("Should return created person")
    void testPersonCreate() {
        PersonCreateDto personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "123456789081");
        PersonCreateDto savedPersonCreateDto = new PersonCreateDto(1L, "John Doe", "30", "123456789081");

        when(personService.personCreate(any(PersonCreateDto.class))).thenReturn(savedPersonCreateDto);

        ResponseEntity<PersonCreateDto> response = personController.create(personCreateDto);

        assertNotNull(response);
        assertEquals(201, response.getStatusCode().value());
        assertNotNull(response.getHeaders().getLocation());
        assertEquals("/create/1", response.getHeaders().getLocation().getPath());
        assertEquals(savedPersonCreateDto, response.getBody());
    }

    @Test
    @DisplayName("Should test list method pagination")
    void testPersonListPaginated() {
        Pageable pageable = Pageable.unpaged();

        PersonCreateDto personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "123456789");
        Page<PersonCreateDto> personCreateDtoPage = new PageImpl<>(Collections.singletonList(personCreateDto));

        when(personService.personListPagination(pageable)).thenReturn(personCreateDtoPage);

        ResponseEntity<Page<PersonCreateDto>> response = personController.list(pageable);

        assertEquals(personCreateDtoPage, response.getBody());
    }

    @Test
    @DisplayName("Should test list method with empty pagination")
    void testListWithEmptyPagination() {
        Page<PersonCreateDto> emptyPage = new PageImpl<>(Collections.emptyList());

        when(personService.personListPagination(pageable)).thenReturn(emptyPage);

        ResponseEntity<Page<PersonCreateDto>> response = personController.list(pageable);

        assertEquals(emptyPage, response.getBody());
    }

    @Test
    @DisplayName("Should test list method")
    void testList() {
        List<PersonCreateDto> personList = Collections.singletonList(new PersonCreateDto(1L, "John Doe", "30", "123456789"));

        when(personService.personList()).thenReturn(personList);

        ResponseEntity<List<PersonCreateDto>> response = personController.list();

        assertEquals(personList, response.getBody());
    }

    @Test
    @DisplayName("Should test list method with empty list")
    void testListEmpty() {
        List<PersonCreateDto> personList = Collections.emptyList();

        when(personService.personList()).thenReturn(personList);

        ResponseEntity<List<PersonCreateDto>> response = personController.list();

        assertEquals(personList, response.getBody());        
    }

    @Test
    @DisplayName("Should update person information with valid ID")
    void testUpdatePersonWithValidId() {
        Long id = 1L;
        PersonCreateDto personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "12345678912");
        
        when(personService.personUpdate(any(Long.class), any(PersonCreateDto.class))).thenReturn(personCreateDto);

        ResponseEntity<PersonCreateDto> response = personController.update(id, personCreateDto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(personCreateDto.name(), Objects.requireNonNull(response.getBody()).name());
        assertEquals(personCreateDto.age(), response.getBody().age());
        assertEquals(personCreateDto.cpf(), response.getBody().cpf());
    }

    @Test
    @DisplayName("Should throw exception when ID is not found")
    void testUpdatePersonWithInvalidId() {
        Long id = 1L;
        PersonCreateDto personCreateDto = new PersonCreateDto(1L, "John Doe", "30", "12345678912");

        when(personService.personUpdate(any(Long.class), any(PersonCreateDto.class)))
                .thenThrow(new ErroRequest("ID não encontrado"));

        assertThrows(ErroRequest.class, () -> personController.update(id, personCreateDto));
    }

    @Test
    @DisplayName("Should return 200 OK with valid ID")
    void testDetailWithValidId() {
        Long id = 1L;
        PersonCreateDto personDetail = new PersonCreateDto(1L, "John Doe", "30", "12345678912");
        when(personService.detailPerson(id)).thenReturn(Optional.of(personDetail));

        ResponseEntity<PersonCreateDto> response = personController.detail(id);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(personDetail, response.getBody());
    }

    @Test
    @DisplayName("Should return 404 Not Found with invalid ID")
    void testDetailWithInvalidId() {
        Long id = 2L;
        when(personService.detailPerson(id)).thenReturn(Optional.empty());

        ResponseEntity<PersonCreateDto> response = personController.detail(id);

        assertEquals(404, response.getStatusCode().value());
    }
}
