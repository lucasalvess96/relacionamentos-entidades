package com.lombok.praticas.estudos.person;

import com.lombok.praticas.estudos.person.Dto.PersonCreateDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {
    
    @Mock
    private PersonService personService;
    
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
}
